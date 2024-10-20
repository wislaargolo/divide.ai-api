package com.ufrn.imd.divide.ai.service;

import com.ufrn.imd.divide.ai.dto.request.UserTransactionCreateRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.UserTransactionUpdateRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.UserTransactionResponseDTO;
import com.ufrn.imd.divide.ai.model.UserTransaction;

public interface UserTransactionService {
    UserTransactionResponseDTO save(UserTransactionCreateRequestDTO dto);

    UserTransactionResponseDTO update(Long transactionId, UserTransactionUpdateRequestDTO dto);

    void delete(Long transactionId);

    UserTransactionResponseDTO findById(Long userId);

    UserTransaction findByIdIfExists(Long id);
}
