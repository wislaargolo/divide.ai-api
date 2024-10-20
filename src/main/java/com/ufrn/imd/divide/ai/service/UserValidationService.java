package com.ufrn.imd.divide.ai.service;

import com.ufrn.imd.divide.ai.exception.ForbiddenOperationException;

import com.ufrn.imd.divide.ai.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserValidationService {

    private final JwtService jwtService;

    public UserValidationService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public void validateUser(Long userId, String errorMessage) {
        Long userIdToken = jwtService.extractUserIdFromRequest();

        if (!userId.equals(userIdToken)) {
            throw (errorMessage != null && !errorMessage.trim().isEmpty())
                    ? new ForbiddenOperationException(errorMessage)
                    : new ForbiddenOperationException();
        }

    }

    public void validateUser(Long userId) {
        validateUser(userId, null);
    }
}
