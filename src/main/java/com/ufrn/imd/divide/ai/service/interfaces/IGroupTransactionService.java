package com.ufrn.imd.divide.ai.service.interfaces;

import com.ufrn.imd.divide.ai.dto.request.GroupTransactionCreateRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.GroupTransactionResponseDTO;

public interface IGroupTransactionService {

    GroupTransactionResponseDTO save(GroupTransactionCreateRequestDTO dto);
}
