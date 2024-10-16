package com.ufrn.imd.divide.ai.controller;

import com.ufrn.imd.divide.ai.dto.request.AuthRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.ApiResponseDTO;
import com.ufrn.imd.divide.ai.dto.response.AuthResponseDTO;
import com.ufrn.imd.divide.ai.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> authenticate(@RequestBody @Valid AuthRequestDTO request) {
        ApiResponseDTO<AuthResponseDTO> response = new ApiResponseDTO<>(
                true,
                "Authentication completed successfully",
                authenticationService.authenticate(request),
                null);

        return ResponseEntity.ok(response);
    }
}
