package com.ufrn.imd.divide.ai.controller;

import com.ufrn.imd.divide.ai.dto.response.ApiResponseDTO;
import com.ufrn.imd.divide.ai.dto.response.DebtResponseDTO;
import com.ufrn.imd.divide.ai.service.interfaces.IDebtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/debts")
public class DebtController {
    private final IDebtService debtService;

    public DebtController(IDebtService debtService) {
        this.debtService = debtService;
    }

    @GetMapping("/details/{groupId}")
    public ResponseEntity<ApiResponseDTO<List<DebtResponseDTO>>> getDebtsByGroupId(@PathVariable Long groupId) {
        ApiResponseDTO<List<DebtResponseDTO>> response = new ApiResponseDTO<>(
                true,
                "Todos os débitos da transação do grupo recuperados com sucesso.",
                debtService.getDebtsByGroupId(groupId),
                null
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{debtId}/paid-at")
    public ResponseEntity<ApiResponseDTO<DebtResponseDTO>> updatePaidAt(
            @PathVariable Long debtId,
            @RequestParam LocalDateTime paidAt) {

        ApiResponseDTO<DebtResponseDTO> response = new ApiResponseDTO<>(
                true,
                "Campo pago atualizado com sucesso.",
                debtService.updatePaidAt(debtId, paidAt),
                null
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/history/{groupTransactionId}")
    public ResponseEntity<ApiResponseDTO<List<DebtResponseDTO>>> getDebtHistoryByGroupTransaction(@PathVariable Long groupTransactionId) {

        ApiResponseDTO<List<DebtResponseDTO>> response = new ApiResponseDTO<>(
                true,
                "Histórico recuparado com sucesso",
                debtService.getDebtHistoryByGroupTransaction(groupTransactionId),
                null
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



}
