package com.ufrn.imd.divide.ai.service;

import com.ufrn.imd.divide.ai.dto.request.DebtUpdateRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.GroupTransactionCreateRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.GroupTransactionUpdateRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.GroupTransactionResponseDTO;
import com.ufrn.imd.divide.ai.exception.BusinessException;
import com.ufrn.imd.divide.ai.exception.ResourceNotFoundException;
import com.ufrn.imd.divide.ai.mapper.DebtMapper;
import com.ufrn.imd.divide.ai.mapper.GroupTransactionMapper;
import com.ufrn.imd.divide.ai.model.*;
import com.ufrn.imd.divide.ai.repository.GroupTransactionRepository;
import com.ufrn.imd.divide.ai.service.interfaces.*;
import com.ufrn.imd.divide.ai.util.AttributeUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GroupTransactionService implements IGroupTransactionService {

    private final GroupTransactionRepository groupTransactionRepository;
    private final IUserService userService;
    private final IGroupService groupService;
    private final GroupTransactionMapper groupTransactionMapper;
    private final IUserValidationService userValidationService;
    private final IDebtService debtService;
    private final DebtMapper debtMapper;

    public GroupTransactionService(GroupTransactionRepository groupTransactionRepository,
            IUserService userService,
            IGroupService groupService,
            GroupTransactionMapper groupTransactionMapper,
            IUserValidationService userValidationService,
            IDebtService debtService,
            DebtMapper debtMapper) {
        this.groupTransactionRepository = groupTransactionRepository;
        this.userService = userService;
        this.groupService = groupService;
        this.groupTransactionMapper = groupTransactionMapper;
        this.userValidationService = userValidationService;
        this.debtService = debtService;
        this.debtMapper = debtMapper;
    }

    @Transactional
    @Override
    public GroupTransactionResponseDTO save(GroupTransactionCreateRequestDTO dto) {

        GroupTransaction groupTransaction = groupTransactionMapper.toEntity(dto);

        Group group = groupService.findByIdIfExists(dto.groupId());
        User createdBy = userService.findById(dto.createdBy());
        groupTransaction.setGroup(group);
        groupTransaction.setCreatedBy(createdBy);

        validateBeforeSave(groupTransaction);

        GroupTransaction savedGroupTransaction = groupTransactionRepository.save(groupTransaction);

        List<Debt> debts = debtService.saveDebts(dto.debts(), savedGroupTransaction);
        savedGroupTransaction.setDebts(debts);

        return groupTransactionMapper.toDTO(savedGroupTransaction);
    }

    private void validateBeforeSave(GroupTransaction groupTransaction) {
        userValidationService.validateUser(groupTransaction.getCreatedBy().getId());
        validateGroupDescontinued(groupTransaction);

        Double totalDebts = groupTransaction.getDebts().stream()
                .mapToDouble(Debt::getAmount)
                .sum();

        validateTotalDebts(totalDebts, groupTransaction.getAmount());
    }

    @Transactional
    @Override
    public GroupTransactionResponseDTO update(Long transactionId, GroupTransactionUpdateRequestDTO dto) {
        GroupTransaction groupTransaction = findByIdIfExists(transactionId);
        BeanUtils.copyProperties(dto, groupTransaction, AttributeUtils.getNullOrBlankPropertyNames(dto));

        validateBeforeUpdate(dto, groupTransaction);

        GroupTransaction updatedGroupTransaction = groupTransactionRepository.save(groupTransaction);

        List<Debt> debts = debtService.updateDebts(dto.debts());
        updatedGroupTransaction.setDebts(debts);

        return groupTransactionMapper.toDTO(updatedGroupTransaction);

    }

    @Transactional
    @Override
    public List<GroupTransactionResponseDTO> findAll(Long groupId) {
        Group group = groupService.findByIdIfExists(groupId);
        List<GroupTransaction> groupTransactions = groupTransactionRepository.findByGroupId(group.getId());
        return groupTransactions.stream()
                .map(groupTransactionMapper::toDTO)
                .toList();
    }

    @Transactional
    @Override
    public String delete(Long groupId, Long transactionId) {
        Group group = groupService.findByIdIfExists(groupId);
        GroupTransaction groupTransaction = findByIdIfExists(transactionId);

        if (!groupTransaction.getGroup().getId().equals(group.getId())) {
            throw new ResourceNotFoundException(
                    "Despesa de grupo com id " + transactionId + " não encontrada para o grupo com id " + groupId);
        }
        groupTransactionRepository.delete(groupTransaction);

        return "Despesa de grupo com id " + transactionId + " deletada com sucesso.";
    }

    @Override
    public GroupTransactionResponseDTO findById(Long transactionId) {
        GroupTransaction groupTransaction = findByIdIfExists(transactionId);
        return groupTransactionMapper.toDTO(groupTransaction);
    }

    private GroupTransaction findByIdIfExists(Long id) {
        return groupTransactionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                "Despesa de grupo com id " + id + " não encontrado."));
    }

    private void validateBeforeUpdate(GroupTransactionUpdateRequestDTO dto, GroupTransaction groupTransaction) {

        userValidationService.validateUser(
                groupTransaction.getCreatedBy().getId(),
                "Apenas o dono da despesa em grupo pode alterá-la.");
        validateGroupDescontinued(groupTransaction);
        validateDebtsList(groupTransaction.getDebts(), dto.debts());

        Double totalDebts = dto.debts().stream()
                .mapToDouble(DebtUpdateRequestDTO::amount)
                .sum();

        validateTotalDebts(totalDebts, groupTransaction.getAmount());
    }

    private void validateGroupDescontinued(GroupTransaction groupTransaction) {
        if(groupTransaction.getGroup().isDiscontinued()) {
            throw new BusinessException(
                    "Não é possível gerenciar despesas um grupo descontinuado.",
                    HttpStatus.BAD_REQUEST);
        }
    }

    private void validateTotalDebts(Double totalDebts, Double dto) {
        if (!totalDebts.equals(dto)) {
            throw new BusinessException(
                    "A soma dos valores das dívidas deve ser igual ao total da transação do grupo.",
                    HttpStatus.BAD_REQUEST);
        }
    }

    private void validateDebtsList(List<Debt> databaseDebts, List<DebtUpdateRequestDTO> dtoDebts) {

        Set<Debt> debts = new HashSet<>(databaseDebts);
        Set<Debt> updatedDebts = new HashSet<>(dtoDebts.stream().map(debtMapper::toEntity).toList());

        if (!debts.equals(updatedDebts)) {
            throw new BusinessException(
                    "A lista de dívidas da transação não corresponde a lista original",
                    HttpStatus.BAD_REQUEST);
        }
    }

}
