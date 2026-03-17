package com.bankhub.transaction.dto;

import com.bankhub.transaction.entity.TransactionStatus;
import com.bankhub.transaction.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponseDto {
    private Long id;
    private String transactionId;
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
    private TransactionType transactionType;
    private TransactionStatus status;
    private String description;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
    private String referenceNumber;

    public TransactionResponseDto() {}

    public TransactionResponseDto(Long id, String transactionId, String fromAccount, String toAccount, BigDecimal amount, TransactionType transactionType, TransactionStatus status, String description, Long userId, LocalDateTime createdAt, LocalDateTime processedAt, String referenceNumber) {
        this.id = id;
        this.transactionId = transactionId;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.transactionType = transactionType;
        this.status = status;
        this.description = description;
        this.userId = userId;
        this.createdAt = createdAt;
        this.processedAt = processedAt;
        this.referenceNumber = referenceNumber;
    }

    public static TransactionResponseBuilder builder() { return new TransactionResponseBuilder(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public String getFromAccount() { return fromAccount; }
    public void setFromAccount(String fromAccount) { this.fromAccount = fromAccount; }
    public String getToAccount() { return toAccount; }
    public void setToAccount(String toAccount) { this.toAccount = toAccount; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }
    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }
    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }

    public static class TransactionResponseBuilder {
        private Long id;
        private String transactionId;
        private String fromAccount;
        private String toAccount;
        private BigDecimal amount;
        private TransactionType transactionType;
        private TransactionStatus status;
        private String description;
        private Long userId;
        private LocalDateTime createdAt;
        private LocalDateTime processedAt;
        private String referenceNumber;

        TransactionResponseBuilder() {}

        public TransactionResponseBuilder id(Long id) { this.id = id; return this; }
        public TransactionResponseBuilder transactionId(String transactionId) { this.transactionId = transactionId; return this; }
        public TransactionResponseBuilder fromAccount(String fromAccount) { this.fromAccount = fromAccount; return this; }
        public TransactionResponseBuilder toAccount(String toAccount) { this.toAccount = toAccount; return this; }
        public TransactionResponseBuilder amount(BigDecimal amount) { this.amount = amount; return this; }
        public TransactionResponseBuilder transactionType(TransactionType transactionType) { this.transactionType = transactionType; return this; }
        public TransactionResponseBuilder type(TransactionType transactionType) { this.transactionType = transactionType; return this; }
        public TransactionResponseBuilder status(TransactionStatus status) { this.status = status; return this; }
        public TransactionResponseBuilder description(String description) { this.description = description; return this; }
        public TransactionResponseBuilder userId(Long userId) { this.userId = userId; return this; }
        public TransactionResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public TransactionResponseBuilder processedAt(LocalDateTime processedAt) { this.processedAt = processedAt; return this; }
        public TransactionResponseBuilder referenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; return this; }

        public TransactionResponseDto build() {
            return new TransactionResponseDto(id, transactionId, fromAccount, toAccount, amount, transactionType, status, description, userId, createdAt, processedAt, referenceNumber);
        }
    }
}