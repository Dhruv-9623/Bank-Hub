package com.bankhub.transaction.service;

import com.bankhub.transaction.dto.*;
import com.bankhub.transaction.entity.Transaction;
import com.bankhub.transaction.entity.TransactionStatus;
import com.bankhub.transaction.entity.TransactionType;
import com.bankhub.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountServiceClient accountServiceClient;
    private final TransactionEventPublisher eventPublisher;
    private final Random random = new Random();

    public TransactionResponseDto transferMoney(TransferRequestDto transferRequest) {
        String transactionId = generateTransactionId();
        String referenceNumber = generateReferenceNumber();

        log.info("Starting money transfer: {} from {} to {} amount: {}",
                transactionId, transferRequest.getFromAccount(),
                transferRequest.getToAccount(), transferRequest.getAmount());

        // Create pending transaction
        Transaction transaction = Transaction.builder()
                .transactionId(transactionId)
                .fromAccount(transferRequest.getFromAccount())
                .toAccount(transferRequest.getToAccount())
                .amount(transferRequest.getAmount())
                .transactionType(TransactionType.TRANSFER)
                .status(TransactionStatus.PENDING)
                .description(transferRequest.getDescription())
                .userId(transferRequest.getUserId())
                .referenceNumber(referenceNumber)
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        try {
            // Step 1: Validate accounts exist and have sufficient funds
            AccountDto fromAccount = accountServiceClient.getAccountByNumber(transferRequest.getFromAccount());
            AccountDto toAccount = accountServiceClient.getAccountByNumber(transferRequest.getToAccount());

            if (!fromAccount.getIsActive() || !toAccount.getIsActive()) {
                throw new RuntimeException("One or both accounts are inactive");
            }

            if (fromAccount.getBalance().compareTo(transferRequest.getAmount()) < 0) {
                throw new RuntimeException("Insufficient funds in source account");
            }

            // Step 2: Update transaction status to processing
            savedTransaction.setStatus(TransactionStatus.PROCESSING);
            transactionRepository.save(savedTransaction);

            // Step 3: Withdraw from source account
            BalanceUpdateDto withdrawDto = new BalanceUpdateDto(
                    transferRequest.getAmount(),
                    "Transfer to " + transferRequest.getToAccount() + " - " + transactionId
            );
            accountServiceClient.withdrawMoney(transferRequest.getFromAccount(), withdrawDto);

            // Step 4: Deposit to destination account
            BalanceUpdateDto depositDto = new BalanceUpdateDto(
                    transferRequest.getAmount(),
                    "Transfer from " + transferRequest.getFromAccount() + " - " + transactionId
            );
            accountServiceClient.depositMoney(transferRequest.getToAccount(), depositDto);

            // Step 5: Mark transaction as completed
            savedTransaction.setStatus(TransactionStatus.COMPLETED);
            savedTransaction.setProcessedAt(LocalDateTime.now());
            Transaction completedTransaction = transactionRepository.save(savedTransaction);

            log.info("Money transfer completed successfully: {}", transactionId);

            eventPublisher.publishTransactionEvent(completedTransaction);
            log.info(" Transaction event published to Kafka for notifications");

            return mapToTransactionResponseDto(completedTransaction);

        } catch (Exception e) {
            // Mark transaction as failed
            savedTransaction.setStatus(TransactionStatus.FAILED);
            savedTransaction.setProcessedAt(LocalDateTime.now());
            transactionRepository.save(savedTransaction);

            log.error("Money transfer failed: {} - Error: {}", transactionId, e.getMessage());
            throw new RuntimeException("Transfer failed: " + e.getMessage());
        }
    }

    public List<TransactionResponseDto> getTransactionHistory(Long userId) {
        List<Transaction> transactions = transactionRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return transactions.stream()
                .map(this::mapToTransactionResponseDto)
                .collect(Collectors.toList());
    }

    public List<TransactionResponseDto> getAccountTransactions(String accountNumber) {
        List<Transaction> transactions = transactionRepository.findByFromAccountOrToAccount(accountNumber, accountNumber);
        return transactions.stream()
                .map(this::mapToTransactionResponseDto)
                .collect(Collectors.toList());
    }

    public TransactionResponseDto getTransactionById(String transactionId) {
        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found: " + transactionId));

        return mapToTransactionResponseDto(transaction);
    }

    private String generateTransactionId() {
        String transactionId;
        do {
            transactionId = "TXN" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
        } while (transactionRepository.existsByTransactionId(transactionId));

        return transactionId;
    }

    private String generateReferenceNumber() {
        return "REF" + String.format("%012d", random.nextInt(1000000000));
    }

    private TransactionResponseDto mapToTransactionResponseDto(Transaction transaction) {
        return TransactionResponseDto.builder()
                .id(transaction.getId())
                .transactionId(transaction.getTransactionId())
                .fromAccount(transaction.getFromAccount())
                .toAccount(transaction.getToAccount())
                .amount(transaction.getAmount())
                .transactionType(transaction.getTransactionType())
                .status(transaction.getStatus())
                .description(transaction.getDescription())
                .userId(transaction.getUserId())
                .createdAt(transaction.getCreatedAt())
                .processedAt(transaction.getProcessedAt())
                .referenceNumber(transaction.getReferenceNumber())
                .build();
    }
}