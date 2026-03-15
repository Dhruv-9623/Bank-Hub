package com.bankhub.notification.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionEventDto {
    private String transactionId;
    private Long userId;
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
    private String transactionType;
    private String status;
    private String description;
    private LocalDateTime timestamp;

    public TransactionEventDto() {}

    public TransactionEventDto(String transactionId, Long userId, String fromAccount, String toAccount,
                              BigDecimal amount, String transactionType, String status, String description, LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.transactionType = transactionType;
        this.status = status;
        this.description = description;
        this.timestamp = timestamp;
    }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getFromAccount() { return fromAccount; }
    public void setFromAccount(String fromAccount) { this.fromAccount = fromAccount; }
    public String getToAccount() { return toAccount; }
    public void setToAccount(String toAccount) { this.toAccount = toAccount; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}