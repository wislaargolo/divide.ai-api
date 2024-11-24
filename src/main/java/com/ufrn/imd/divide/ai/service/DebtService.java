package com.ufrn.imd.divide.ai.service;

import com.ufrn.imd.divide.ai.dto.request.DebtRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.DebtUpdateRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.DebtResponseDTO;
import com.ufrn.imd.divide.ai.exception.BusinessException;
import com.ufrn.imd.divide.ai.exception.ResourceNotFoundException;
import com.ufrn.imd.divide.ai.mapper.DebtMapper;
import com.ufrn.imd.divide.ai.model.Debt;
import com.ufrn.imd.divide.ai.model.GroupTransaction;
import com.ufrn.imd.divide.ai.model.User;
import com.ufrn.imd.divide.ai.repository.DebtRepository;
import com.ufrn.imd.divide.ai.repository.GroupTransactionRepository;
import com.ufrn.imd.divide.ai.service.interfaces.IDebtService;
import com.ufrn.imd.divide.ai.service.interfaces.IUserService;
import com.ufrn.imd.divide.ai.util.AttributeUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DebtService implements IDebtService {

    private final DebtRepository debtRepository;
    private final GroupTransactionRepository groupTransactionRepository;
    private final IUserService userService;
    private final DebtMapper debtMapper;
    private final UserValidationService userValidationService;
    public DebtService(DebtRepository debtRepository,
                       IUserService userService,
                       DebtMapper debtMapper,
                       GroupTransactionRepository groupTransactionRepository,
                       UserValidationService userValidationService) {
        this.debtRepository = debtRepository;
        this.userService = userService;
        this.debtMapper = debtMapper;
        this.groupTransactionRepository = groupTransactionRepository;
        this.userValidationService = userValidationService;
    }

    @Override
    public List<Debt> saveDebts(List<DebtRequestDTO> debtRequestDTOs, GroupTransaction savedGroupTransaction) {
        List<Debt> debts = new ArrayList<>();
        for (DebtRequestDTO debtDTO : debtRequestDTOs) {
            Debt debt = debtMapper.toEntity(debtDTO);

            User user = userService.findById(debtDTO.userId());
            debt.setUser(user);
            debt.setGroupTransaction(savedGroupTransaction);
            debts.add(debt);
        }
        return debtRepository.saveAll(debts);
    }

    @Override
    public List<Debt> updateDebts(List<DebtUpdateRequestDTO> debtRequestDTOS) {
        List<Debt> debts = new ArrayList<>();
        for (DebtUpdateRequestDTO debtDTO : debtRequestDTOS) {
            Debt debt = findById(debtDTO.id());
            validateBeforeUpdate(debt, debtDTO.amount());
            BeanUtils.copyProperties(debtDTO, debt, AttributeUtils.getNullOrBlankPropertyNames(debtDTO));
            debts.add(debt);
        }
        return debtRepository.saveAll(debts);
    }

    @Override
    public Debt findById(Long id) {
        return debtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                "Dívida de ID " + id + " não encontrada."
        ));
    }



    public List<DebtResponseDTO> getDebtsByGroupId(Long groupTransactionId) {
        if (!groupTransactionRepository.existsById(groupTransactionId)) {
            throw new BusinessException(
                    "Transação de grupo com ID " + groupTransactionId + " não encontrado.",
                    HttpStatus.NOT_FOUND
            );
        }

        return debtRepository.findByGroupTransaction_IdAndActiveTrue(groupTransactionId)
                .stream()
                .map(debtMapper::toDTO)
                .collect(Collectors.toList());
    }


    public DebtResponseDTO updatePaidAt(Long debtId, LocalDateTime paidAt) {
        Debt debt = debtRepository.findById(debtId)
                .orElseThrow(() -> new EntityNotFoundException("Débito com ID " + debtId + " não encontrado."));

        userValidationService.validateUser(debt.getGroupTransaction().getCreatedBy().getId(),"Apenas o dono da transação do grupo pode setar se a despesa está paga ou não.");

        debt.setPaidAt(paidAt);
        return debtMapper.toDTO(debtRepository.save(debt));
    }

    public List<DebtResponseDTO> getDebtHistoryByGroupTransaction(Long groupTransactionId) {
        return debtRepository.findByGroupTransactionIdOrderByPaidAtDescThenCreatedAtDesc(groupTransactionId)
                .stream()
                .map(debtMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void validateBeforeUpdate(Debt debt, Double dtoAmount) {
        if(!debt.getAmount().equals(dtoAmount) && debt.getPaidAt() != null) {
            throw new BusinessException(
                    "Não é possível alterar o valor de uma dívida paga",
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
