package com.bankhub.transaction.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id", unique = true, nullable = false)
    private String transactionId;

    @Column(name = "from_account")
    private String fromAccount;

    @Column(name = "to_account", nullable = false)
    private String toAccount;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status = TransactionStatus.PENDING;

    private String description;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "reference_number")
    private String referenceNumber;

    @Version
    private Long version;

    public Transaction() {}

    public Transaction(String transactionId, String fromAccount, String toAccount, BigDecimal amount, TransactionType transactionType, TransactionStatus status, String description, Long userId, LocalDateTime createdAt, LocalDateTime processedAt, String referenceNumber) {
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

    public static TransactionBuilder builder() {
        return new TransactionBuilder();
    }

    // Getters and Setters
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
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    // Builder class
    public static class TransactionBuilder {
        private Long id;
        private String transactionId;
        private String fromAccount;
        private String toAccount;
        private BigDecimal amount;
        private TransactionType transactionType;
        private TransactionStatus status = TransactionStatus.PENDING;
        private String description;
        private Long userId;
        private LocalDateTime createdAt;
        private LocalDateTime processedAt;
        private String referenceNumber;

        TransactionBuilder() {}

        public TransactionBuilder id(Long id) { this.id = id; return this; }
        public TransactionBuilder transactionId(String transactionId) { this.transactionId = transactionId; return this; }
        public TransactionBuilder fromAccount(String fromAccount) { this.fromAccount = fromAccount; return this; }
        public TransactionBuilder toAccount(String toAccount) { this.toAccount = toAccount; return this; }
        public TransactionBuilder amount(BigDecimal amount) { this.amount = amount; return this; }
        public TransactionBuilder transactionType(TransactionType transactionType) { this.transactionType = transactionType; return this; }
        public TransactionBuilder status(TransactionStatus status) { this.status = status; return this; }
        public TransactionBuilder description(String description) { this.description = description; return this; }
        public TransactionBuilder userId(Long userId) { this.userId = userId; return this; }
        public TransactionBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public TransactionBuilder processedAt(LocalDateTime processedAt) { this.processedAt = processedAt; return this; }
        public TransactionBuilder referenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; return this; }

        public Transaction build() {
            Transaction transaction = new Transaction();
            transaction.id = this.id;
            transaction.transactionId = this.transactionId;
            transaction.fromAccount = this.fromAccount;
            transaction.toAccount = this.toAccount;
            transaction.amount = this.amount;
            transaction.transactionType = this.transactionType;
            transaction.status = this.status;
            transaction.description = this.description;
            transaction.userId = this.userId;
            transaction.createdAt = this.createdAt;
            transaction.processedAt = this.processedAt;
            transaction.referenceNumber = this.referenceNumber;
            return transaction;
        }
    }
}