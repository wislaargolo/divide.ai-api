package com.ufrn.imd.divide.ai.service;

import com.ufrn.imd.divide.ai.dto.request.DebtRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.GroupTransactionCreateRequestDTO;
import com.ufrn.imd.divide.ai.mapper.DebtMapper;
import com.ufrn.imd.divide.ai.model.Debt;
import com.ufrn.imd.divide.ai.model.GroupTransaction;
import com.ufrn.imd.divide.ai.model.User;
import com.ufrn.imd.divide.ai.repository.DebtRepository;
import com.ufrn.imd.divide.ai.service.interfaces.IDebtService;
import com.ufrn.imd.divide.ai.service.interfaces.IUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
}
