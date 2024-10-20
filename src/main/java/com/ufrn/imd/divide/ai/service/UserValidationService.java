package com.ufrn.imd.divide.ai.service;

public interface UserValidationService {
    void validateUser(Long userId, String errorMessage);
    void validateUser(Long userId);
}
