package com.ufrn.imd.divide.ai.service.interfaces;

import com.ufrn.imd.divide.ai.dto.request.DebtRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.DebtUpdateRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.DebtResponseDTO;
import com.ufrn.imd.divide.ai.model.Debt;
import com.ufrn.imd.divide.ai.model.GroupTransaction;

import java.time.LocalDateTime;
import java.util.List;

public interface IDebtService {

    List<Debt> saveDebts(List<DebtRequestDTO> debtRequestDTOs, GroupTransaction groupTransaction);

    List<Debt> updateDebts(List<DebtUpdateRequestDTO> debtRequestDTOS);

    Debt findById(Long id);

    List<DebtResponseDTO> getDebtsByGroupId(Long groupTransactionId);


    DebtResponseDTO updatePaidAt(Long debtId, LocalDateTime paidAt);

    List<DebtResponseDTO> getDebtHistoryByGroupTransaction(Long groupTransactionId);


}
