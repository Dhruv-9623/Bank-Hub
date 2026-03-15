package com.bankhub.transaction.controller;

import com.bankhub.transaction.dto.TransactionResponseDto;
import com.bankhub.transaction.dto.TransferRequestDto;
import com.bankhub.transaction.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Validated
@CrossOrigin(origins = "*")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TransactionResponseDto> transferMoney(
            @Valid @RequestBody TransferRequestDto transferRequest) {

        System.out.println("Money transfer request: " + transferRequest.getFromAccount() + " to " + transferRequest.getToAccount() + " amount: " + transferRequest.getAmount());

        TransactionResponseDto transaction = transactionService.transferMoney(transferRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionResponseDto>> getUserTransactions(
            @PathVariable Long userId) {

        System.out.println("Fetching transaction history for user: " + userId);

        List<TransactionResponseDto> transactions = transactionService.getTransactionHistory(userId);

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<List<TransactionResponseDto>> getAccountTransactions(
            @PathVariable String accountNumber) {

        System.out.println("Fetching transactions for account: " + accountNumber);

        List<TransactionResponseDto> transactions = transactionService.getAccountTransactions(accountNumber);

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDto> getTransactionById(
            @PathVariable String transactionId) {

        System.out.println("Fetching transaction details: " + transactionId);

        TransactionResponseDto transaction = transactionService.getTransactionById(transactionId);

        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Transaction Service is working! 💰");
    }
}