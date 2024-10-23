package com.ufrn.imd.divide.ai.mapper;

import com.ufrn.imd.divide.ai.dto.request.GroupCreateRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.GroupUpdateRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.GroupResponseDTO;
import com.ufrn.imd.divide.ai.model.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface GroupMapper {

    @Mapping(target = "createdBy", ignore = true)
    Group toEntity(GroupCreateRequestDTO groupCreateRequestDTO);

    Group toEntity(GroupUpdateRequestDTO groupUpdateRequestDTO);

    GroupResponseDTO toDto(Group group);
}
