package com.ufrn.imd.divide.ai.controller;

import com.ufrn.imd.divide.ai.dto.request.UserRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.ApiResponseDTO;
import com.ufrn.imd.divide.ai.dto.response.UserResponseDTO;
import com.ufrn.imd.divide.ai.service.UserService;
import com.ufrn.imd.divide.ai.util.validation.OnCreate;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseDTO<?>> delete(@PathVariable Long userId) {
        userService.delete(userId);
        ApiResponseDTO<?> response = new ApiResponseDTO<>(
                true,
                "User deleted successfully",
                null,
                null
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> update(
            @PathVariable Long userId,
            @RequestBody @Valid UserRequestDTO dto) {

        ApiResponseDTO<UserResponseDTO> response = new ApiResponseDTO<>(
                true,
                "User updated successfully.",
                userService.update(dto, userId),
                null
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> save(
            @Validated({Default.class, OnCreate.class})
            @RequestBody UserRequestDTO dto){

        ApiResponseDTO<UserResponseDTO> response = new ApiResponseDTO<>(
                true,
                "User created successfully.",
                userService.save(dto),
                null
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
