package com.ufrn.imd.divide.ai.controller;

import com.ufrn.imd.divide.ai.dto.request.GroupCreateRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.GroupUpdateRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.JoinGroupRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.ApiResponseDTO;
import com.ufrn.imd.divide.ai.dto.response.GroupResponseDTO;
import com.ufrn.imd.divide.ai.model.Group;
import com.ufrn.imd.divide.ai.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<GroupResponseDTO>> save(
            @Valid @RequestBody GroupCreateRequestDTO dto) {

        ApiResponseDTO<GroupResponseDTO> response = new ApiResponseDTO<>(
                true,
                "Group created successfully.",
                groupService.save(dto),
                null
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<GroupResponseDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody GroupUpdateRequestDTO dto) {

        ApiResponseDTO<GroupResponseDTO> response = new ApiResponseDTO<>(
                true,
                "Group updated successfully.",
                groupService.update(id, dto),
                null
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<?>> delete(@PathVariable Long id) {
        groupService.delete(id);

        ApiResponseDTO<?> response = new ApiResponseDTO<>(
                true,
                "Group deleted successfully.",
                null,
                null
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<GroupResponseDTO>> findById(@PathVariable Long id) {
        GroupResponseDTO groupResponse = groupService.findById(id);

        ApiResponseDTO<GroupResponseDTO> response = new ApiResponseDTO<>(
                true,
                "Group retrieved successfully.",
                groupResponse,
                null
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponseDTO<List<GroupResponseDTO>>> findAllByUserId(
            @PathVariable Long userId) {

        ApiResponseDTO<List<GroupResponseDTO>> response = new ApiResponseDTO<>(
                true,
                "Groups retrieved successfully.",
                groupService.findAllByUserId(userId),
                null
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/join")
    public ResponseEntity<ApiResponseDTO<GroupResponseDTO>> joinGroupByCode(
            @Valid @RequestBody JoinGroupRequestDTO dto) {

        ApiResponseDTO<GroupResponseDTO> response = new ApiResponseDTO<>(
                true,
                "User joined group successfully.",
                groupService.joinGroupByCode(dto),
                null
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{groupId}/user/{userId}/leave")
    public ResponseEntity<ApiResponseDTO<GroupResponseDTO>> leaveGroup(
            @PathVariable Long groupId,
            @PathVariable Long userId) {

            ApiResponseDTO<GroupResponseDTO> response = new ApiResponseDTO<>(
                    true,
                    "User has successfully left the group",
                    groupService.leaveGroup(groupId, userId),
                    null
            );
            return ResponseEntity.ok(response);
    }
}
