package com.ufrn.imd.divide.ai.service;

import com.ufrn.imd.divide.ai.dto.request.DebtRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.DebtUpdateRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.GroupTransactionCreateRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.GroupTransactionUpdateRequestDTO;
import com.ufrn.imd.divide.ai.exception.BusinessException;
import com.ufrn.imd.divide.ai.exception.ResourceNotFoundException;
import com.ufrn.imd.divide.ai.mapper.DebtMapper;
import com.ufrn.imd.divide.ai.model.Debt;
import com.ufrn.imd.divide.ai.model.GroupTransaction;
import com.ufrn.imd.divide.ai.model.User;
import com.ufrn.imd.divide.ai.repository.DebtRepository;
import com.ufrn.imd.divide.ai.service.interfaces.IDebtService;
import com.ufrn.imd.divide.ai.service.interfaces.IUserService;
import com.ufrn.imd.divide.ai.util.AttributeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DebtService implements IDebtService {

    private final DebtRepository debtRepository;
    private final IUserService userService;
    private final DebtMapper debtMapper;

    public DebtService(DebtRepository debtRepository,
                       IUserService userService,
                       DebtMapper debtMapper) {
        this.debtRepository = debtRepository;
        this.userService = userService;
        this.debtMapper = debtMapper;
    }

    public List<Debt> saveDebts(GroupTransactionCreateRequestDTO dto, GroupTransaction savedGroupTransaction) {
        List<Debt> debts = new ArrayList<>();
        for (DebtRequestDTO debtDTO : dto.debts()) {
            Debt debt = debtMapper.toEntity(debtDTO);

            User user = userService.findById(debtDTO.userId());
            debt.setUser(user);
            debt.setGroupTransaction(savedGroupTransaction);
            debts.add(debt);
        }
        return debtRepository.saveAll(debts);
    }

    @Override
    public List<Debt> updateDebts(GroupTransactionUpdateRequestDTO dto) {
        List<Debt> debts = new ArrayList<>();
        for (DebtUpdateRequestDTO debtDTO : dto.debts()) {
            Debt debt = findById(debtDTO.id());
            validateBeforeUpdate(debt, debtDTO.amount());
            BeanUtils.copyProperties(debtDTO, debt, AttributeUtils.getNullOrBlankPropertyNames(debtDTO));
            debts.add(debt);
        }
        return debtRepository.saveAll(debts);
    }

    private void validateBeforeUpdate(Debt debt, Double dtoAmount) {
        if(!debt.getAmount().equals(dtoAmount) && debt.getPaidAt() != null) {
            throw new BusinessException(
                   "Não é possível alterar o valor de uma dívida paga",
                   HttpStatus.BAD_REQUEST
            );
        }
    }

    public Debt findById(Long id) {
        return debtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                "Dívida de ID " + id + " não encontrada."
        ));
    }



}
