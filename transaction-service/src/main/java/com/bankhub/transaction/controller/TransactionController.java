package com.bankhub.transaction.controller;

import com.bankhub.transaction.dto.TransactionResponseDto;
import com.bankhub.transaction.dto.TransferRequestDto;
import com.bankhub.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Validated
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TransactionResponseDto> transferMoney(
            @Valid @RequestBody TransferRequestDto transferRequest) {

        log.info("Money transfer request: {} to {} amount: {}",
                transferRequest.getFromAccount(),
                transferRequest.getToAccount(),
                transferRequest.getAmount());

        TransactionResponseDto transaction = transactionService.transferMoney(transferRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionResponseDto>> getUserTransactions(
            @PathVariable Long userId) {

        log.info("Fetching transaction history for user: {}", userId);

        List<TransactionResponseDto> transactions = transactionService.getTransactionHistory(userId);

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<List<TransactionResponseDto>> getAccountTransactions(
            @PathVariable String accountNumber) {

        log.info("Fetching transactions for account: {}", accountNumber);

        List<TransactionResponseDto> transactions = transactionService.getAccountTransactions(accountNumber);

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDto> getTransactionById(
            @PathVariable String transactionId) {

        log.info("Fetching transaction details: {}", transactionId);

        TransactionResponseDto transaction = transactionService.getTransactionById(transactionId);

        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Transaction Service is working! 💰");
    }
}