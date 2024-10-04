package com.ufrn.imd.divide.ai.service;

import com.ufrn.imd.divide.ai.dto.request.UserRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.UserResponseDTO;
import com.ufrn.imd.divide.ai.mapper.UserMapper;
import com.ufrn.imd.divide.ai.model.User;
import com.ufrn.imd.divide.ai.repository.UserRepository;
import com.ufrn.imd.divide.ai.exception.BusinessException;
import jakarta.transaction.Transactional;
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

    @Transactional
    public UserResponseDTO save(UserRequestDTO dto){
        User entity = userMapper.toEntity(dto);

        validateBeforeSave(entity);

        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        return userMapper.toDto(userRepository.save(entity));
    }

    public void validateBeforeSave(User entity){
        validateEmail(entity.getEmail(), entity.getId());
        validatePhoneNumber(entity.getPhoneNumber(), entity.getId());
    }

    private void validateEmail(String email, Long id){
        Optional<User> user = userRepository.findByEmailIgnoreCase(email);
        if (user.isPresent() && (id == null || !user.get().getId().equals(id))) {
            throw new BusinessException(
                    "Invalid email: " + email + ". A user is already registered with this email.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validatePhoneNumber(String phone, Long id) {
        Optional<User> user = userRepository.findByPhoneNumberIgnoreCase(phone);
        if (user.isPresent() && (id == null || !user.get().getId().equals(id))) {
            throw new BusinessException(
                    "Invalid phone number: " + phone + ". A user is already registered with this phone number.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

}
