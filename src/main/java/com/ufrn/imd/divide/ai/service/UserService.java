package com.ufrn.imd.divide.ai.service;

import com.ufrn.imd.divide.ai.dto.request.UserCreateRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.UserUpdateRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.UserResponseDTO;
import com.ufrn.imd.divide.ai.exception.ResourceNotFoundException;
import com.ufrn.imd.divide.ai.mapper.UserMapper;
import com.ufrn.imd.divide.ai.model.User;
import com.ufrn.imd.divide.ai.repository.UserRepository;
import com.ufrn.imd.divide.ai.exception.BusinessException;
import com.ufrn.imd.divide.ai.service.interfaces.IGroupService;
import com.ufrn.imd.divide.ai.service.interfaces.IUserService;
import com.ufrn.imd.divide.ai.service.interfaces.IUserValidationService;
import com.ufrn.imd.divide.ai.util.AttributeUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final IUserValidationService IUserValidationService;
    private final IGroupService groupService;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       UserMapper userMapper,
                       IUserValidationService IUserValidationService,
                       IGroupService groupService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.IUserValidationService = IUserValidationService;
        this.groupService = groupService;
    }

    @Transactional
    @Override
    public void delete(Long userId) {
        IUserValidationService.validateUser(userId);
        User user = findById(userId);

        groupService.validateAndUpdateGroupsForUserDeletion(user);

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
        return userRepository.findByIdAndActiveTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário de ID " + userId + " não encontrado."
                ));
    }

    private void validateBeforeUpdate(User entity) {
        IUserValidationService.validateUser(entity.getId());
        validateBeforeSave(entity);
    }

    private void validateBeforeSave(User entity) {
        validateEmail(entity.getEmail(), entity.getId());
        validatePhoneNumber(entity.getPhoneNumber(), entity.getId());
    }

    private void validateEmail(String email, Long id) {
        Optional<User> user = userRepository.findByEmailIgnoreCaseAndActiveTrue(email);
        if (user.isPresent() && (id == null || !user.get().getId().equals(id))) {
            throw new BusinessException(
                    "E-mail inválido: " + email + ". Um usuário cadastrado já utiliza este e-mail.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validatePhoneNumber(String phone, Long id) {
        Optional<User> user = userRepository.findByPhoneNumberAndActiveTrue(phone);
        if (user.isPresent() && (id == null || !user.get().getId().equals(id))) {
            throw new BusinessException(
                    "Número de telefone inválido: " + phone + ". Um usuário cadastrado já utiliza este número de telefone.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

}
