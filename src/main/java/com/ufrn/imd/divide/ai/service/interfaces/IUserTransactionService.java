package com.ufrn.imd.divide.ai.service.interfaces;

import com.ufrn.imd.divide.ai.dto.request.UserTransactionCreateRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.UserTransactionUpdateRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.UserTransactionByMonthResponseDTO;
import com.ufrn.imd.divide.ai.dto.response.UserTransactionResponseDTO;
import com.ufrn.imd.divide.ai.model.UserTransaction;

import java.util.List;

public interface IUserTransactionService {
    UserTransactionResponseDTO save(UserTransactionCreateRequestDTO dto);

    UserTransactionResponseDTO update(Long transactionId, UserTransactionUpdateRequestDTO dto);

    void delete(Long transactionId);

    UserTransactionResponseDTO findById(Long userId);

    List<UserTransactionByMonthResponseDTO> getUserTransactionsGroupedByMonth(Long userId);

    UserTransaction findByIdIfExists(Long id);

    List<UserTransactionResponseDTO> findAllByUserId(Long userId);
}
