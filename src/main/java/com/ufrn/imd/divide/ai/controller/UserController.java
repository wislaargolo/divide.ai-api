package com.ufrn.imd.divide.ai.controller;

import com.ufrn.imd.divide.ai.dto.request.AuthRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.UserRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.ApiResponseDTO;
import com.ufrn.imd.divide.ai.dto.response.AuthResponseDTO;
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

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponseDTO<UserResponseDTO> save(@Valid @RequestBody UserRequestDTO dto){
        return new ApiResponseDTO<>(
                        true,
                        "Sucess: User created successfully.",
                         userService.save(dto),
                        null
        );
    }

}
