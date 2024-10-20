package com.ufrn.imd.divide.ai.service;

import com.ufrn.imd.divide.ai.dto.request.GroupCreateRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.GroupUpdateRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.JoinGroupRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.GroupResponseDTO;
import com.ufrn.imd.divide.ai.exception.BusinessException;
import com.ufrn.imd.divide.ai.exception.ResourceNotFoundException;
import com.ufrn.imd.divide.ai.mapper.GroupMapper;
import com.ufrn.imd.divide.ai.model.Debt;
import com.ufrn.imd.divide.ai.model.Group;
import com.ufrn.imd.divide.ai.model.User;
import com.ufrn.imd.divide.ai.repository.DebtRepository;
import com.ufrn.imd.divide.ai.repository.GroupRepository;
import com.ufrn.imd.divide.ai.util.AttributeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final UserService userService;
    private final DebtRepository debtRepository;
    private final UserValidationService userValidationService;


    public GroupService(GroupRepository groupRepository,
                        GroupMapper groupMapper,
                        UserService userService, DebtRepository debtRepository,
                        UserValidationService userValidationService) {
        this.groupRepository = groupRepository;
        this.groupMapper = groupMapper;
        this.userService = userService;
        this.debtRepository = debtRepository;
        this.userValidationService = userValidationService;
    }

    public void delete(Long groupId) {
        Group group = findByIdIfExists(groupId);
        userValidationService.validateUser(group.getCreatedBy().getId(), "Only the owner of the group can delete it");
        group.setActive(false);
        groupRepository.save(group);
    }


    public GroupResponseDTO save(GroupCreateRequestDTO dto) {
        userValidationService.validateUser(dto.createdBy());
        User creator = userService.findById(dto.createdBy());
        validateBeforeSave(dto, creator);

        Group group = groupMapper.toEntity(dto);

        group.setCreatedBy(creator);
        group.setCode(generateUniqueCode());
        group.setMembers(new ArrayList<>());
        group.getMembers().add(creator);

        return groupMapper.toDto(groupRepository.save(group));
    }

    private void validateBeforeSave(GroupCreateRequestDTO dto, User creator) {
        Optional<Group> existingGroup = groupRepository.findByNameAndCreatedBy(dto.name(), creator);
        if (existingGroup.isPresent()) {
            throw new BusinessException(
                    "You already have a group with the name: " + dto.name(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public GroupResponseDTO update(Long groupId, GroupUpdateRequestDTO dto) {
        Group group = findByIdIfExists(groupId);
        userValidationService.validateUser(group.getCreatedBy().getId(), "Only the owner of the group can update it");

        BeanUtils.copyProperties(dto, group, AttributeUtils.getNullOrBlankPropertyNames(dto));
        return groupMapper.toDto(groupRepository.save(group));
    }


    public List<GroupResponseDTO> findAllByUserId(Long userId) {
        userService.findById(userId);
        List<Group> groups = groupRepository.findByMembers_Id(userId);
        return groups.stream()
                .map(groupMapper::toDto)
                .collect(Collectors.toList());
    }

    public GroupResponseDTO findById(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Group with ID " + groupId + " not found"
                ));

        return groupMapper.toDto(group);
    }

    public Group findByIdIfExists(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Group with ID " + groupId + " not found"
                ));
    }

    public GroupResponseDTO joinGroupByCode(JoinGroupRequestDTO dto) {
        Group group = validateBeforeJoin(dto.code(), dto.userId());

        User user = userService.findById(dto.userId());
        group.getMembers().add(user);

        return groupMapper.toDto(groupRepository.save(group));
    }

    public Group validateBeforeJoin(String code, Long userId) {
        Group group = groupRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Group with code " + code + " not found"
                ));

        User user = userService.findById(userId);

        if (group.getMembers().contains(user)) {
            throw new BusinessException(
                    "User is already a member of the group.",
                    HttpStatus.BAD_REQUEST
            );
        }

        return group;
    }

    public GroupResponseDTO leaveGroup(Long groupId, Long userId) {
        userValidationService.validateUser(userId);
        User user = userService.findById(userId);
        Group group = findByIdIfExists(groupId);

        validateBeforeLeave(group, user);

        group.getMembers().remove(user);
        return groupMapper.toDto(groupRepository.save(group));
    }

    public void validateBeforeLeave(Group group, User user) {

        if (group.getCreatedBy().equals(user)) {
            throw new BusinessException(
                    "The group owner cannot leave the group. Use the remove group instead.",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (!group.getMembers().contains(user)) {
            throw new BusinessException(
                    "User is not a member of this group.",
                    HttpStatus.BAD_REQUEST
            );
        }

        List<Debt> unpaidDebts = debtRepository.findByUserAndGroupTransaction_GroupAndPaidAtIsNull(user, group);
        if (!unpaidDebts.isEmpty()) {
            throw new BusinessException(
                    "User has unpaid debts in this group and cannot leave.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private String generateUniqueCode() {
        String code;
        do {
            String uuid = UUID.randomUUID().toString();
            code = uuid.substring(0, 6);
        } while (groupRepository.existsByCode(code));
        return code;
    }



}
