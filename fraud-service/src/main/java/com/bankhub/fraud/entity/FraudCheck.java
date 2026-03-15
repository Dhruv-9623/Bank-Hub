package com.bankhub.fraud.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fraud_checks")
public class FraudCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false)
    private RiskLevel riskLevel;

    @Column(name = "risk_score", precision = 5, scale = 2)
    private BigDecimal riskScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "decision", nullable = false)
    private FraudDecision decision = FraudDecision.PENDING;

    @Column(name = "fraud_reasons", columnDefinition = "TEXT")
    private String fraudReasons;

    @Column(name = "processing_time_ms")
    private Long processingTimeMs;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(name = "reviewed_by")
    private String reviewedBy;

    @Version
    private Long version;

    public FraudCheck() {}

    public FraudCheck(String transactionId, Long userId, String accountNumber, BigDecimal amount, RiskLevel riskLevel, BigDecimal riskScore, FraudDecision decision, String fraudReasons, Long processingTimeMs, LocalDateTime createdAt, LocalDateTime reviewedAt, String reviewedBy) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.riskLevel = riskLevel;
        this.riskScore = riskScore;
        this.decision = decision;
        this.fraudReasons = fraudReasons;
        this.processingTimeMs = processingTimeMs;
        this.createdAt = createdAt;
        this.reviewedAt = reviewedAt;
        this.reviewedBy = reviewedBy;
    }

    public static FraudCheckBuilder builder() {
        return new FraudCheckBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public RiskLevel getRiskLevel() { return riskLevel; }
    public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }
    public BigDecimal getRiskScore() { return riskScore; }
    public void setRiskScore(BigDecimal riskScore) { this.riskScore = riskScore; }
    public FraudDecision getDecision() { return decision; }
    public void setDecision(FraudDecision decision) { this.decision = decision; }
    public String getFraudReasons() { return fraudReasons; }
    public void setFraudReasons(String fraudReasons) { this.fraudReasons = fraudReasons; }
    public Long getProcessingTimeMs() { return processingTimeMs; }
    public void setProcessingTimeMs(Long processingTimeMs) { this.processingTimeMs = processingTimeMs; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
    public String getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(String reviewedBy) { this.reviewedBy = reviewedBy; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    public static class FraudCheckBuilder {
        private Long id;
        private String transactionId;
        private Long userId;
        private String accountNumber;
        private BigDecimal amount;
        private RiskLevel riskLevel;
        private BigDecimal riskScore;
        private FraudDecision decision = FraudDecision.PENDING;
        private String fraudReasons;
        private Long processingTimeMs;
        private LocalDateTime createdAt;
        private LocalDateTime reviewedAt;
        private String reviewedBy;

        FraudCheckBuilder() {}

        public FraudCheckBuilder id(Long id) { this.id = id; return this; }
        public FraudCheckBuilder transactionId(String transactionId) { this.transactionId = transactionId; return this; }
        public FraudCheckBuilder userId(Long userId) { this.userId = userId; return this; }
        public FraudCheckBuilder accountNumber(String accountNumber) { this.accountNumber = accountNumber; return this; }
        public FraudCheckBuilder amount(BigDecimal amount) { this.amount = amount; return this; }
        public FraudCheckBuilder riskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; return this; }
        public FraudCheckBuilder riskScore(BigDecimal riskScore) { this.riskScore = riskScore; return this; }
        public FraudCheckBuilder decision(FraudDecision decision) { this.decision = decision; return this; }
        public FraudCheckBuilder fraudReasons(String fraudReasons) { this.fraudReasons = fraudReasons; return this; }
        public FraudCheckBuilder processingTimeMs(Long processingTimeMs) { this.processingTimeMs = processingTimeMs; return this; }
        public FraudCheckBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public FraudCheckBuilder reviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; return this; }
        public FraudCheckBuilder reviewedBy(String reviewedBy) { this.reviewedBy = reviewedBy; return this; }

        public FraudCheck build() {
            FraudCheck fraudCheck = new FraudCheck();
            fraudCheck.id = this.id;
            fraudCheck.transactionId = this.transactionId;
            fraudCheck.userId = this.userId;
            fraudCheck.accountNumber = this.accountNumber;
            fraudCheck.amount = this.amount;
            fraudCheck.riskLevel = this.riskLevel;
            fraudCheck.riskScore = this.riskScore;
            fraudCheck.decision = this.decision;
            fraudCheck.fraudReasons = this.fraudReasons;
            fraudCheck.processingTimeMs = this.processingTimeMs;
            fraudCheck.createdAt = this.createdAt;
            fraudCheck.reviewedAt = this.reviewedAt;
            fraudCheck.reviewedBy = this.reviewedBy;
            return fraudCheck;
        }
    }
}