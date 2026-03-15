package com.bankhub.fraud.dto;

import com.bankhub.fraud.entity.FraudDecision;
import com.bankhub.fraud.entity.RiskLevel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class FraudCheckResponseDto {
    private Long id;
    private String transactionId;
    private Long userId;
    private String accountNumber;
    private BigDecimal amount;
    private RiskLevel riskLevel;
    private BigDecimal riskScore;
    private FraudDecision decision;
    private List<String> fraudReasons;
    private Long processingTimeMs;
    private LocalDateTime createdAt;
    private boolean isBlocked;
    private String recommendedAction;

    public FraudCheckResponseDto() {}

    public FraudCheckResponseDto(Long id, String transactionId, Long userId, String accountNumber, BigDecimal amount, RiskLevel riskLevel, BigDecimal riskScore, FraudDecision decision, List<String> fraudReasons, Long processingTimeMs, LocalDateTime createdAt, boolean isBlocked, String recommendedAction) {
        this.id = id;
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
        this.isBlocked = isBlocked;
        this.recommendedAction = recommendedAction;
    }

    public static FraudCheckResponseBuilder builder() { return new FraudCheckResponseBuilder(); }

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
    public List<String> getFraudReasons() { return fraudReasons; }
    public void setFraudReasons(List<String> fraudReasons) { this.fraudReasons = fraudReasons; }
    public Long getProcessingTimeMs() { return processingTimeMs; }
    public void setProcessingTimeMs(Long processingTimeMs) { this.processingTimeMs = processingTimeMs; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public boolean isBlocked() { return isBlocked; }
    public void setBlocked(boolean blocked) { this.isBlocked = blocked; }
    public String getRecommendedAction() { return recommendedAction; }
    public void setRecommendedAction(String recommendedAction) { this.recommendedAction = recommendedAction; }

    public static class FraudCheckResponseBuilder {
        private Long id;
        private String transactionId;
        private Long userId;
        private String accountNumber;
        private BigDecimal amount;
        private RiskLevel riskLevel;
        private BigDecimal riskScore;
        private FraudDecision decision;
        private List<String> fraudReasons;
        private Long processingTimeMs;
        private LocalDateTime createdAt;
        private boolean isBlocked;
        private String recommendedAction;

        FraudCheckResponseBuilder() {}

        public FraudCheckResponseBuilder id(Long id) { this.id = id; return this; }
        public FraudCheckResponseBuilder transactionId(String transactionId) { this.transactionId = transactionId; return this; }
        public FraudCheckResponseBuilder userId(Long userId) { this.userId = userId; return this; }
        public FraudCheckResponseBuilder accountNumber(String accountNumber) { this.accountNumber = accountNumber; return this; }
        public FraudCheckResponseBuilder amount(BigDecimal amount) { this.amount = amount; return this; }
        public FraudCheckResponseBuilder riskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; return this; }
        public FraudCheckResponseBuilder riskScore(BigDecimal riskScore) { this.riskScore = riskScore; return this; }
        public FraudCheckResponseBuilder decision(FraudDecision decision) { this.decision = decision; return this; }
        public FraudCheckResponseBuilder fraudReasons(List<String> fraudReasons) { this.fraudReasons = fraudReasons; return this; }
        public FraudCheckResponseBuilder processingTimeMs(Long processingTimeMs) { this.processingTimeMs = processingTimeMs; return this; }
        public FraudCheckResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public FraudCheckResponseBuilder isBlocked(boolean isBlocked) { this.isBlocked = isBlocked; return this; }
        public FraudCheckResponseBuilder recommendedAction(String recommendedAction) { this.recommendedAction = recommendedAction; return this; }

        public FraudCheckResponseDto build() {
            return new FraudCheckResponseDto(id, transactionId, userId, accountNumber, amount, riskLevel, riskScore, decision, fraudReasons, processingTimeMs, createdAt, isBlocked, recommendedAction);
        }
    }
}