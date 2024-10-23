package com.ufrn.imd.divide.ai.service.interfaces;

public interface IUserValidationService {
    void validateUser(Long userId, String errorMessage);
    void validateUser(Long userId);
}
