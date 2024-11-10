package com.ufrn.imd.divide.ai.service.interfaces;

import java.util.List;

import com.ufrn.imd.divide.ai.dto.request.GroupTransactionCreateRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.GroupTransactionUpdateRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.GroupTransactionResponseDTO;

public interface IGroupTransactionService {

    GroupTransactionResponseDTO save(GroupTransactionCreateRequestDTO dto);

    GroupTransactionResponseDTO update(Long id, GroupTransactionUpdateRequestDTO dto);

    List<GroupTransactionResponseDTO> findAll(Long groupId);

    GroupTransactionResponseDTO findById(Long transactionId);
    
    String delete(Long groupId, Long transactionId);
}
