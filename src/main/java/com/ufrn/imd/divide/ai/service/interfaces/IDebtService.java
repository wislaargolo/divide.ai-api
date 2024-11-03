package com.ufrn.imd.divide.ai.service.interfaces;

import com.ufrn.imd.divide.ai.dto.request.GroupTransactionCreateRequestDTO;
import com.ufrn.imd.divide.ai.model.Debt;
import com.ufrn.imd.divide.ai.model.GroupTransaction;

import java.util.List;

public interface IDebtService {

    public List<Debt> saveDebts(GroupTransactionCreateRequestDTO dto, GroupTransaction savedGroupTransaction);

}
