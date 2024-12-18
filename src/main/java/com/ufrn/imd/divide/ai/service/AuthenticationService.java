package com.ufrn.imd.divide.ai.service;

import com.ufrn.imd.divide.ai.dto.request.AuthRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.AuthResponseDTO;
import com.ufrn.imd.divide.ai.model.User;
import com.ufrn.imd.divide.ai.model.UserDetailsImpl;
import com.ufrn.imd.divide.ai.repository.UserRepository;
import com.ufrn.imd.divide.ai.exception.ResourceNotFoundException;
import com.ufrn.imd.divide.ai.service.interfaces.IAuthenticationService;
import com.ufrn.imd.divide.ai.service.interfaces.IJwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements IAuthenticationService {

    private final UserRepository userRepository;
    private final IJwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationService(UserRepository userRepository,
                                 IJwtService jwtService,
                                 AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthResponseDTO authenticate(AuthRequestDTO request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(), request.password()
                )
        );

        User user = userRepository.findByEmailIgnoreCaseAndActiveTrue(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        String token = jwtService.generateToken(new UserDetailsImpl(user));

        return new AuthResponseDTO(token);
    }
}
