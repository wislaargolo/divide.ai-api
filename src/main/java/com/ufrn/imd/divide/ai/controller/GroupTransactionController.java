package com.ufrn.imd.divide.ai.controller;

import com.ufrn.imd.divide.ai.dto.request.GroupCreateRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.GroupTransactionCreateRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.GroupTransactionUpdateRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.ApiResponseDTO;
import com.ufrn.imd.divide.ai.dto.response.GroupResponseDTO;
import com.ufrn.imd.divide.ai.dto.response.GroupTransactionResponseDTO;
import com.ufrn.imd.divide.ai.service.interfaces.IGroupTransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/group-transactions")
public class GroupTransactionController {

    private final IGroupTransactionService groupTransactionService;


    public GroupTransactionController(IGroupTransactionService groupTransactionService) {
        this.groupTransactionService = groupTransactionService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<GroupTransactionResponseDTO>> save(
            @Valid @RequestBody GroupTransactionCreateRequestDTO dto) {

        ApiResponseDTO<GroupTransactionResponseDTO> response = new ApiResponseDTO<>(
                true,
                "Despesa em grupo criada com sucesso.",
                groupTransactionService.save(dto),
                null
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<GroupTransactionResponseDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody GroupTransactionUpdateRequestDTO dto) {

        ApiResponseDTO<GroupTransactionResponseDTO> response = new ApiResponseDTO<>(
                true,
                "Despesa em grupo atualizada com sucesso.",
                groupTransactionService.update(id, dto),
                null
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
