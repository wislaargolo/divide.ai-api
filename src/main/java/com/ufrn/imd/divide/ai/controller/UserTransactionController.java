package com.ufrn.imd.divide.ai.controller;

import com.ufrn.imd.divide.ai.dto.request.UserTransactionCreateRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.UserTransactionUpdateRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.ApiResponseDTO;
import com.ufrn.imd.divide.ai.dto.response.UserTransactionByMonthResponseDTO;
import com.ufrn.imd.divide.ai.dto.response.UserTransactionResponseDTO;
import com.ufrn.imd.divide.ai.service.interfaces.IUserTransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user-transactions")
public class UserTransactionController {

    private final IUserTransactionService userTransactionService;

    public UserTransactionController(IUserTransactionService userTransactionService) {
        this.userTransactionService = userTransactionService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<UserTransactionResponseDTO>> save(
            @Valid @RequestBody UserTransactionCreateRequestDTO dto) {

        ApiResponseDTO<UserTransactionResponseDTO> response = new ApiResponseDTO<>(
                true,
                "Transação criada com sucesso.",
                userTransactionService.save(dto),
                null
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UserTransactionResponseDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody UserTransactionUpdateRequestDTO dto) {

        ApiResponseDTO<UserTransactionResponseDTO> response = new ApiResponseDTO<>(
                true,
                "Transação atualizada com sucesso.",
                userTransactionService.update(id, dto),
                null
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<?>> delete(@PathVariable Long id) {
        userTransactionService.delete(id);

        ApiResponseDTO<?> response = new ApiResponseDTO<>(
                true,
                "Transação removida com sucesso.",
                null,
                null
        );

        return ResponseEntity.ok(response);
    }
    @GetMapping("/transactions/{userId}")
    public ResponseEntity<ApiResponseDTO<List<UserTransactionResponseDTO>>> getUserTransactions(@PathVariable Long userId) {

        ApiResponseDTO<List<UserTransactionResponseDTO>> response = new ApiResponseDTO<>(
                true,
                "Transações recuperadas com sucesso.",
                userTransactionService.findAllByUserId(userId),
                null
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/transactions/grouped-by-month/{userId}")
    public ResponseEntity<ApiResponseDTO<List<UserTransactionByMonthResponseDTO>>> getTransactionsGroupedByMonth(@PathVariable Long userId) {
        ApiResponseDTO<List<UserTransactionByMonthResponseDTO>> response = new ApiResponseDTO<>(
                true,
                "Transações por mês recuperadas com sucesso.",
                userTransactionService.getUserTransactionsGroupedByMonth(userId),
                null
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UserTransactionResponseDTO>> findById(@PathVariable Long id) {
        UserTransactionResponseDTO transactionResponse = userTransactionService.findById(id);
        ApiResponseDTO<UserTransactionResponseDTO> response = new ApiResponseDTO<>(
                true,
                "Transação recuperada com sucesso.",
                transactionResponse,
                null
        );

        return ResponseEntity.ok(response);
    }

}
