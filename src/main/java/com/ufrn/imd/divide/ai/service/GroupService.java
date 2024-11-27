package com.ufrn.imd.divide.ai.service;

import com.ufrn.imd.divide.ai.dto.request.GroupCreateRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.GroupUpdateRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.JoinGroupRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.GroupResponseDTO;
import com.ufrn.imd.divide.ai.exception.BusinessException;
import com.ufrn.imd.divide.ai.exception.ResourceNotFoundException;
import com.ufrn.imd.divide.ai.mapper.GroupMapper;
import com.ufrn.imd.divide.ai.model.Group;
import com.ufrn.imd.divide.ai.model.User;
import com.ufrn.imd.divide.ai.repository.DebtRepository;
import com.ufrn.imd.divide.ai.repository.GroupRepository;
import com.ufrn.imd.divide.ai.repository.UserRepository;
import com.ufrn.imd.divide.ai.service.interfaces.IGroupService;
import com.ufrn.imd.divide.ai.service.interfaces.IUserService;
import com.ufrn.imd.divide.ai.service.interfaces.IUserValidationService;
import com.ufrn.imd.divide.ai.util.AttributeUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GroupService implements IGroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final IUserService userService;
    private final DebtRepository debtRepository;
    private final IUserValidationService userValidationService;


    public GroupService(GroupRepository groupRepository,
                        GroupMapper groupMapper,
                        @Lazy IUserService userService,
                        DebtRepository debtRepository,
                        IUserValidationService userValidationService) {
        this.groupRepository = groupRepository;
        this.groupMapper = groupMapper;
        this.userService = userService;
        this.debtRepository = debtRepository;
        this.userValidationService = userValidationService;
    }

    @Override
    public void delete(Long groupId) {
        Group group = findByIdIfExists(groupId);
        userValidationService.validateUser(group.getCreatedBy().getId(),
                "Apenas o dono do grupo pode removê-lo.");
        groupRepository.delete(group);
    }

    @Override
    @Transactional
    public GroupResponseDTO save(GroupCreateRequestDTO dto) {

        validateBeforeSave(dto.createdBy());

        Group group = groupMapper.toEntity(dto);

        User creator = new User(dto.createdBy());
        group.setCreatedBy(creator);
        group.setCode(generateUniqueCode());
        group.setMembers(new ArrayList<>());
        group.getMembers().add(creator);

        return groupMapper.toDto(groupRepository.save(group));
    }

    @Override
    public GroupResponseDTO update(Long groupId, GroupUpdateRequestDTO dto) {
        Group group = findByIdIfExists(groupId);

        validateBeforeSave(group.getCreatedBy().getId());
        BeanUtils.copyProperties(dto, group, AttributeUtils.getNullOrBlankPropertyNames(dto));

        return groupMapper.toDto(groupRepository.save(group));
    }

    private void validateBeforeSave(Long createdBy) {
        userService.findById(createdBy);
        userValidationService.validateUser(createdBy);
    }

    @Override
    public List<GroupResponseDTO> findAllByUserId(Long userId) {
        userService.findById(userId);
        List<Group> groups = groupRepository.findByMembersId(userId);

        return groups.
                stream().map(groupMapper::toDto)
                .collect(Collectors.toList());

    }

    @Override
    public GroupResponseDTO findById(Long groupId) {
        Group group = findByIdIfExists(groupId);
        return groupMapper.toDto(group);
    }

    @Override
    public Group findByIdIfExists(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Grupo de ID " + groupId + " não encontrado."
                ));
    }

    @Override
    public GroupResponseDTO joinGroupByCode(JoinGroupRequestDTO dto) {
        Group group = findByCodeIfExists(dto.code());
        User user = userService.findById(dto.userId());

        validateBeforeJoin(group, user);

        group.getMembers().add(user);

        return groupMapper.toDto(groupRepository.save(group));
    }

    @Override
    public Group findByCodeIfExists(String code) {
        return groupRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Grupo com código " + code + " não encontrado."
                ));
    }

    @Override
    public GroupResponseDTO deleteMember(Long groupId, Long userId) {
        Group group = findByIdIfExists(groupId);
        User user = userService.findById(userId);
        validateBeforeDelete(group, user);

        group.getMembers().remove(user);
        return groupMapper.toDto(groupRepository.save(group));
    }

    @Override
    public GroupResponseDTO leaveGroup(Long groupId, Long userId) {
        Group group = findByIdIfExists(groupId);
        User user = userService.findById(userId);
        validateBeforeLeave(group, user);

        group.getMembers().remove(user);
        return groupMapper.toDto(groupRepository.save(group));
    }

    private String generateUniqueCode() {
        String code;
        do {
            String uuid = UUID.randomUUID().toString();
            code = uuid.substring(0, 6);
        } while (groupRepository.existsByCode(code));
        return code;
    }

    private void validateBeforeJoin(Group group, User user) {
        if(group.isDiscontinued()) {
            throw new BusinessException(
                    "O grupo não aceita mais membros, foi descontinuado.",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (group.getMembers().contains(user)) {
            throw new BusinessException(
                    "Este usuário já é membro do grupo.",
                    HttpStatus.BAD_REQUEST
            );
        }

    }

    private void validateBeforeDelete(Group group, User user) {
        userValidationService.validateUser(
                group.getCreatedBy().getId(),
                "Apenas o dono do grupo pode remover um membro.");

        validateUserRemoval(group, user);
    }

    private void validateBeforeLeave(Group group, User user) {
        userValidationService.validateUser(user.getId());
        validateUserRemoval(group, user);
    }

    private void validateUserRemoval(Group group, User user) {
       validateGroupOwner(group, user);
       validateUserMemberOfGroup(group, user);
       validateUserDebts(group, user);
    }

    private void validateGroupOwner(Group group, User user) {
        if (group.getCreatedBy().equals(user)) {
            throw new BusinessException(
                    "O proprietário do grupo não pode sair ou ser removido. " +
                            "Para encerrar o grupo, é necessário excluí-lo.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateUserMemberOfGroup(Group group, User user) {
        if (!group.getMembers().contains(user)) {
            throw new BusinessException(
                    "O usuário não faz parte deste grupo.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateUserDebts(Group group, User user) {
        if (debtRepository.existsByUserAndGroupTransactionGroupAndPaidAtIsNull(user, group)) {
            throw new BusinessException(
                    "O usuário possui pendências financeiras no grupo " + group.getName() +
                            " e não pode sair ou ser removido até que elas sejam quitadas.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @Override
    public void validateAndUpdateGroupsForUserDeletion(User user) {
        List<Group> groups = groupRepository.findByMembersId(user.getId());

        for (Group group : groups) {
            validateUserDebts(group, user);

            if(group.getCreatedBy().equals(user)) {
                group.setDiscontinued(true);
            }

            group.getMembers().remove(user);
            groupRepository.save(group);

        }
    }

}
