package com.ufrn.imd.divide.ai.service;

import com.ufrn.imd.divide.ai.dto.request.GroupCreateRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.GroupUpdateRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.JoinGroupRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.GroupResponseDTO;
import com.ufrn.imd.divide.ai.model.Group;
import com.ufrn.imd.divide.ai.model.User;

import java.util.List;

public interface GroupService {
    void delete(Long groupId);

    GroupResponseDTO save(GroupCreateRequestDTO dto);

    GroupResponseDTO update(Long groupId, GroupUpdateRequestDTO dto);

    List<GroupResponseDTO> findAllByUserId(Long userId);

    GroupResponseDTO findById(Long groupId);

    Group findByIdIfExists(Long groupId);

    GroupResponseDTO joinGroupByCode(JoinGroupRequestDTO dto);

    Group findByCodeIfExists(String code);

    GroupResponseDTO deleteMember(Long groupId, Long userId);

    GroupResponseDTO leaveGroup(Long groupId, Long userId);

    void validateAndUpdateGroupsForUserDeletion(User user);

}
