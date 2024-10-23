package com.ufrn.imd.divide.ai.service.impl;

import com.ufrn.imd.divide.ai.exception.ForbiddenOperationException;

import com.ufrn.imd.divide.ai.service.JwtService;
import com.ufrn.imd.divide.ai.service.UserValidationService;
import org.springframework.stereotype.Service;

@Service
public class UserValidationServiceImpl implements UserValidationService {

    private final JwtService jwtService;

    public UserValidationServiceImpl(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void validateUser(Long userId, String errorMessage) {
        Long userIdToken = jwtService.extractUserIdFromRequest();

        if (!userId.equals(userIdToken)) {
            throw (errorMessage != null && !errorMessage.trim().isEmpty())
                    ? new ForbiddenOperationException(errorMessage)
                    : new ForbiddenOperationException();
        }

    }

    @Override
    public void validateUser(Long userId) {
        validateUser(userId, null);
    }
}
