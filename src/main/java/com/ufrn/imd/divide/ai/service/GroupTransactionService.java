package com.ufrn.imd.divide.ai.service;

import com.ufrn.imd.divide.ai.dto.request.DebtRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.GroupTransactionCreateRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.GroupTransactionResponseDTO;
import com.ufrn.imd.divide.ai.exception.BusinessException;
import com.ufrn.imd.divide.ai.mapper.DebtMapper;
import com.ufrn.imd.divide.ai.mapper.GroupTransactionMapper;
import com.ufrn.imd.divide.ai.model.*;
import com.ufrn.imd.divide.ai.repository.GroupTransactionRepository;
import com.ufrn.imd.divide.ai.service.interfaces.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupTransactionService implements IGroupTransactionService {

    private final GroupTransactionRepository groupTransactionRepository;
    private final IUserService userService;
    private final IGroupService groupService;
    private final GroupTransactionMapper groupTransactionMapper;
    private final IUserValidationService userValidationService;
    private final IDebtService debtService;

    public GroupTransactionService(GroupTransactionRepository groupTransactionRepository,
                                   IUserService userService,
                                   IGroupService groupService,
                                   GroupTransactionMapper groupTransactionMapper,
                                   IUserValidationService userValidationService,
                                   IDebtService debtService) {
        this.groupTransactionRepository = groupTransactionRepository;
        this.userService = userService;
        this.groupService = groupService;
        this.groupTransactionMapper = groupTransactionMapper;
        this.userValidationService = userValidationService;
        this.debtService = debtService;
    }

    public GroupTransactionResponseDTO save(GroupTransactionCreateRequestDTO dto) {
        validateBeforeSave(dto);

        GroupTransaction groupTransaction = groupTransactionMapper.toEntity(dto);

        Group group = groupService.findByIdIfExists(dto.groupId());
        User createdBy = userService.findById(dto.createdByUserId());
        groupTransaction.setGroup(group);
        groupTransaction.setCreatedBy(createdBy);

        GroupTransaction savedGroupTransaction = groupTransactionRepository.save(groupTransaction);

        if (!dto.debts().isEmpty()) {
            List<Debt> debts = debtService.saveDebts(dto, savedGroupTransaction);
            groupTransaction.setDebts(debts);
        }

        return groupTransactionMapper.toDTO(savedGroupTransaction);
    }

    private void validateBeforeSave(GroupTransactionCreateRequestDTO dto) {
        userValidationService.validateUser(dto.createdByUserId());

        Double totalDebts = dto.debts().stream()
                .mapToDouble(DebtRequestDTO::amount)
                .sum();

        if (!totalDebts.equals(dto.amount())) {
            throw new BusinessException(
                    "A soma dos valores das dívidas deve ser igual ao total da transação do grupo.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

}

