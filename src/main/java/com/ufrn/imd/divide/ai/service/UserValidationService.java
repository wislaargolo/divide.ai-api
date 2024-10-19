package com.ufrn.imd.divide.ai.service;

import com.ufrn.imd.divide.ai.exception.ForbiddenOperationException;

import com.ufrn.imd.divide.ai.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserValidationService {

    private final JwtService jwtService;
    private final UserService userService;

    public UserValidationService(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    public User validateUser(Long userId) {
        Long userIdToken = jwtService.extractUserIdFromRequest();

        if (userId.equals(userIdToken)) {
            return userService.findById(userId);
        } else {
            throw new ForbiddenOperationException();
        }
    }
}
