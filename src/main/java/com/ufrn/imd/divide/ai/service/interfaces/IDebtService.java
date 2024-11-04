package com.ufrn.imd.divide.ai.service.interfaces;

import com.ufrn.imd.divide.ai.dto.request.GroupTransactionCreateRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.GroupTransactionUpdateRequestDTO;
import com.ufrn.imd.divide.ai.model.Debt;
import com.ufrn.imd.divide.ai.model.GroupTransaction;

import java.util.List;

public interface IDebtService {

    List<Debt> saveDebts(GroupTransactionCreateRequestDTO dto, GroupTransaction savedGroupTransaction);

    List<Debt> updateDebts(GroupTransactionUpdateRequestDTO dto);

    Debt findById(Long id);
}