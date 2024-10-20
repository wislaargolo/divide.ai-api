package com.ufrn.imd.divide.ai.service.impl;

import com.ufrn.imd.divide.ai.dto.request.UserCreateRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.UserUpdateRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.UserResponseDTO;
import com.ufrn.imd.divide.ai.exception.ResourceNotFoundException;
import com.ufrn.imd.divide.ai.mapper.UserMapper;
import com.ufrn.imd.divide.ai.model.User;
import com.ufrn.imd.divide.ai.repository.UserRepository;
import com.ufrn.imd.divide.ai.exception.BusinessException;
import com.ufrn.imd.divide.ai.service.UserService;
import com.ufrn.imd.divide.ai.service.UserValidationService;
import com.ufrn.imd.divide.ai.util.AttributeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserValidationService userValidationService;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           UserMapper userMapper,
                           UserValidationService userValidationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.userValidationService = userValidationService;
    }

    @Override
    public void delete(Long userId) {
        userValidationService.validateUser(userId);
        User user = findById(userId);

        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public UserResponseDTO update(UserUpdateRequestDTO dto, Long userId) {
        User user = findById(userId);
        validateBeforeUpdate(user);

        BeanUtils.copyProperties(dto, user, AttributeUtils.getNullOrBlankPropertyNames(dto));
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserResponseDTO save(UserCreateRequestDTO dto) {
        User entity = userMapper.toEntity(dto);
        validateBeforeSave(entity);

        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        return userMapper.toDto(userRepository.save(entity));
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário de ID " + userId + " não encontrado."
                ));
    }

    private void validateBeforeUpdate(User entity) {
        userValidationService.validateUser(entity.getId());
        validateBeforeSave(entity);
    }

    private void validateBeforeSave(User entity) {
        validateEmail(entity.getEmail(), entity.getId());
        validatePhoneNumber(entity.getPhoneNumber(), entity.getId());
    }

    private void validateEmail(String email, Long id) {
        Optional<User> user = userRepository.findByEmailIgnoreCase(email);
        if (user.isPresent() && (id == null || !user.get().getId().equals(id))) {
            throw new BusinessException(
                    "E-mail inválido: " + email + ". Um usuário cadastrado já utiliza este e-mail.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validatePhoneNumber(String phone, Long id) {
        Optional<User> user = userRepository.findByPhoneNumber(phone);
        if (user.isPresent() && (id == null || !user.get().getId().equals(id))) {
            throw new BusinessException(
                    "Número de telefone inválido: " + phone + ". Um usuário cadastrado já utiliza este número de telefone.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

}
