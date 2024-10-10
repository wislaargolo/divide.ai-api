package com.ufrn.imd.divide.ai.controller;

import com.ufrn.imd.divide.ai.dto.request.UserRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.ApiResponseDTO;
import com.ufrn.imd.divide.ai.dto.response.UserResponseDTO;
import com.ufrn.imd.divide.ai.service.UserService;
import com.ufrn.imd.divide.ai.util.validation.OnCreate;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.http.HttpStatus;
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
    public ApiResponseDTO<?> delete(@PathVariable Long userId) {
        userService.delete(userId);
        return new ApiResponseDTO<>(
                true,
                "User deleted successfully",
                null,
                null
        );
    }

    @PutMapping("/{userId}")
    public ApiResponseDTO<UserResponseDTO> update(
            @PathVariable Long userId,
            @RequestBody @Valid UserRequestDTO dto) {

        return new ApiResponseDTO<>(
                true,
                "Sucess: User updated successfully.",
                userService.update(dto, userId),
                null
        );
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponseDTO<UserResponseDTO> save(
            @Validated({Default.class, OnCreate.class})
            @RequestBody UserRequestDTO dto){

        return new ApiResponseDTO<>(
                true,
                "Sucess: User created successfully.",
                 userService.save(dto),
                null
        );
    }

}
