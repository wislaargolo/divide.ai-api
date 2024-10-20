package com.ufrn.imd.divide.ai.service;

import com.ufrn.imd.divide.ai.dto.request.GroupUpdateRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.UserTransactionCreateRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.UserTransactionUpdateRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.GroupResponseDTO;
import com.ufrn.imd.divide.ai.dto.response.UserTransactionResponseDTO;
import com.ufrn.imd.divide.ai.exception.ResourceNotFoundException;
import com.ufrn.imd.divide.ai.mapper.UserTransactionMapper;
import com.ufrn.imd.divide.ai.model.Category;
import com.ufrn.imd.divide.ai.model.Group;
import com.ufrn.imd.divide.ai.model.User;
import com.ufrn.imd.divide.ai.model.UserTransaction;
import com.ufrn.imd.divide.ai.repository.UserTransactionRepository;
import com.ufrn.imd.divide.ai.util.AttributeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class UserTransactionService {

    private final UserTransactionRepository userTransactionRepository;
    private final UserTransactionMapper userTransactionMapper;
    private final UserService userService;
    private final CategoryService categoryService;
    private final UserValidationService userValidationService;


    public UserTransactionService(UserTransactionRepository userTransactionRepository,
                                  UserTransactionMapper userTransactionMapper,
                                  UserService userService,
                                  CategoryService categoryService,
                                  UserValidationService userValidationService) {
        this.userTransactionRepository = userTransactionRepository;
        this.userTransactionMapper = userTransactionMapper;
        this.userService = userService;
        this.categoryService = categoryService;
        this.userValidationService = userValidationService;
    }

    public UserTransactionResponseDTO save(UserTransactionCreateRequestDTO dto) {
        userValidationService.validateUser(dto.userId());
        User user = userService.findById(dto.userId());
        Category category = categoryService.getCategoryByIdIfExists(dto.categoryId());

        UserTransaction userTransaction = userTransactionMapper.toEntity(dto);
        userTransaction.setUser(user);
        userTransaction.setCategory(category);

        return userTransactionMapper.toDto(userTransactionRepository.save(userTransaction));
    }

    public UserTransactionResponseDTO update(Long transactionId,
                                             UserTransactionUpdateRequestDTO dto) {
        UserTransaction userTransaction = findByIdIfExists(transactionId);
        userValidationService.validateUser(userTransaction.getUser().getId(), "Apenas o dono da transação pode atualiza-la.");

        BeanUtils.copyProperties(dto, userTransaction, AttributeUtils.getNullOrBlankPropertyNames(dto));
        return userTransactionMapper.toDto(userTransactionRepository.save(userTransaction));
    }

    public void delete(Long transactionId) {
        UserTransaction userTransaction = findByIdIfExists(transactionId);
        userValidationService.validateUser(userTransaction.getUser().getId(), "Apenas o dono da transação pode removê-la.");
        userTransaction.setActive(false);
        userTransactionRepository.save(userTransaction);
    }

    public UserTransactionResponseDTO findById(Long userId) {
        return userTransactionMapper.toDto(findByIdIfExists(userId));
    }

    public UserTransaction findByIdIfExists(Long id) {
        return userTransactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transação de ID " + id + " não encontrada."
                ));
    }

}
