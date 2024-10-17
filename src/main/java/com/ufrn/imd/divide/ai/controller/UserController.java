package com.ufrn.imd.divide.ai.controller;

import com.ufrn.imd.divide.ai.dto.request.UserCreateRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.UserUpdateRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.ApiResponseDTO;
import com.ufrn.imd.divide.ai.dto.response.UserResponseDTO;
import com.ufrn.imd.divide.ai.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<?>> delete(@PathVariable Long id) {
        userService.delete(id);
        ApiResponseDTO<?> response = new ApiResponseDTO<>(
                true,
                "User deleted successfully",
                null,
                null
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> update(
            @PathVariable Long id,
            @RequestBody @Valid UserUpdateRequestDTO dto) {

        ApiResponseDTO<UserResponseDTO> response = new ApiResponseDTO<>(
                true,
                "User updated successfully.",
                userService.update(dto, id),
                null
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> save(
            @Valid @RequestBody UserCreateRequestDTO dto) {

        ApiResponseDTO<UserResponseDTO> response = new ApiResponseDTO<>(
                true,
                "User created successfully.",
                userService.save(dto),
                null
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
