package com.ufrn.imd.divide.ai.service;

import com.ufrn.imd.divide.ai.dto.request.UserTransactionCreateRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.UserTransactionUpdateRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.UserTransactionResponseDTO;
import com.ufrn.imd.divide.ai.exception.ResourceNotFoundException;
import com.ufrn.imd.divide.ai.mapper.UserTransactionMapper;
import com.ufrn.imd.divide.ai.model.Category;
import com.ufrn.imd.divide.ai.model.User;
import com.ufrn.imd.divide.ai.model.UserTransaction;
import com.ufrn.imd.divide.ai.repository.CategoryRepository;
import com.ufrn.imd.divide.ai.repository.UserTransactionRepository;
import com.ufrn.imd.divide.ai.service.interfaces.ICategoryService;
import com.ufrn.imd.divide.ai.service.interfaces.IUserService;
import com.ufrn.imd.divide.ai.service.interfaces.IUserTransactionService;
import com.ufrn.imd.divide.ai.service.interfaces.IUserValidationService;
import com.ufrn.imd.divide.ai.util.AttributeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserTransactionService implements IUserTransactionService {

    private final UserTransactionRepository userTransactionRepository;
    private final UserTransactionMapper userTransactionMapper;
    private final IUserService userService;
    private final ICategoryService categoryService;
    private final IUserValidationService userValidationService;

    public UserTransactionService(UserTransactionRepository userTransactionRepository,
                                  UserTransactionMapper userTransactionMapper,
                                  IUserService userService,
                                  ICategoryService categoryService,
                                  IUserValidationService userValidationService) {
        this.userTransactionRepository = userTransactionRepository;
        this.userTransactionMapper = userTransactionMapper;
        this.userService = userService;
        this.categoryService = categoryService;
        this.userValidationService = userValidationService;
    }

    @Override
    public UserTransactionResponseDTO save(UserTransactionCreateRequestDTO dto) {
        userValidationService.validateUser(dto.userId());
        User user = userService.findById(dto.userId());
        Category category = categoryService.getCategoryByIdIfExists(dto.categoryId());


        UserTransaction userTransaction = userTransactionMapper.toEntity(dto);
        userTransaction.setUser(user);
        userTransaction.setCategory(category);
        if (category.getExpense()){
            userTransaction.setAmount(-dto.amount());
        }

        return userTransactionMapper.toDto(userTransactionRepository.save(userTransaction));
    }
    @Override
    public List<UserTransactionResponseDTO> findAllByUserId(Long userId) {
        userValidationService.validateUser(userId);

        User user = userService.findById(userId);
        List<UserTransaction> userTransactions = userTransactionRepository.findAllByUser(user);

        return userTransactions.stream()
                .map(userTransactionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserTransactionResponseDTO update(Long transactionId,
                                             UserTransactionUpdateRequestDTO dto) {
        UserTransaction userTransaction = findByIdIfExists(transactionId);
        Category category = categoryService.getCategoryByIdIfExists(dto.categoryId());
        userTransaction.setCategory(category);
        userValidationService.validateUser(userTransaction.getUser().getId(), "Apenas o dono da transação pode atualizá-la.");

        if (dto.paidAt() == null) {
            userTransaction.setPaidAt(null);
        }

        BeanUtils.copyProperties(dto, userTransaction, AttributeUtils.getNullOrBlankPropertyNames(dto));
        if (category.getExpense()){
            userTransaction.setAmount(-dto.amount());
        }
        return userTransactionMapper.toDto(userTransactionRepository.save(userTransaction));
    }


    @Override
    public void delete(Long transactionId) {
        UserTransaction userTransaction = findByIdIfExists(transactionId);
        userValidationService.validateUser(userTransaction.getUser().getId(), "Apenas o dono da transação pode removê-la.");
        userTransaction.setActive(false);
        userTransactionRepository.save(userTransaction);
    }

    @Override
    public UserTransactionResponseDTO findById(Long userId) {
        return userTransactionMapper.toDto(findByIdIfExists(userId));
    }

    @Override
    public UserTransaction findByIdIfExists(Long id) {
        return userTransactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transação de ID " + id + " não encontrada."
                ));
    }
}