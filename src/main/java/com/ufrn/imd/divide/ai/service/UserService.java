package com.ufrn.imd.divide.ai.service;

import com.ufrn.imd.divide.ai.dto.request.UserCreateRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.UserUpdateRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.UserResponseDTO;
import com.ufrn.imd.divide.ai.exception.ResourceNotFoundException;
import com.ufrn.imd.divide.ai.mapper.UserMapper;
import com.ufrn.imd.divide.ai.model.User;
import com.ufrn.imd.divide.ai.repository.UserRepository;
import com.ufrn.imd.divide.ai.exception.BusinessException;
import com.ufrn.imd.divide.ai.util.AttributeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public void delete(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + userId + " not found"));

        user.setActive(false);
        userRepository.save(user);
    }


    public UserResponseDTO update(UserUpdateRequestDTO dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with ID " + userId + " not found"
                ));
//        if (!user.isActive()) {
//            throw new BusinessException("Cannot update an inactive user.", HttpStatus.BAD_REQUEST);
//        }
        BeanUtils.copyProperties(dto, user, AttributeUtils.getNullOrBlankPropertyNames(dto));
        validateBeforeSave(user);
        return userMapper.toDto(userRepository.save(user));
    }

    public UserResponseDTO save(UserCreateRequestDTO dto) {
        User entity = userMapper.toEntity(dto);
        validateBeforeSave(entity);

        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        return userMapper.toDto(userRepository.save(entity));
    }

    public void validateBeforeSave(User entity) {
        validateEmail(entity.getEmail(), entity.getId());
        validatePhoneNumber(entity.getPhoneNumber(), entity.getId());
    }

    private void validateEmail(String email, Long id) {
        Optional<User> user = userRepository.findByEmailIgnoreCase(email);
        if (user.isPresent() && (id == null || !user.get().getId().equals(id))) {
            throw new BusinessException(
                    "Invalid email: " + email + ". A user is already registered with this email.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validatePhoneNumber(String phone, Long id) {
        Optional<User> user = userRepository.findByPhoneNumber(phone);
        if (user.isPresent() && (id == null || !user.get().getId().equals(id))) {
            throw new BusinessException(
                    "Invalid phone number: " + phone + ". A user is already registered with this phone number.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

}
