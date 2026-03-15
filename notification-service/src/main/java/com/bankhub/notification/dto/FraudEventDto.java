package com.bankhub.notification.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FraudEventDto {
    private String transactionId;
    private Long userId;
    private String accountNumber;
    private BigDecimal amount;
    private String riskLevel;
    private BigDecimal riskScore;
    private String decision;
    private String fraudReasons;
    private LocalDateTime timestamp;

    public FraudEventDto() {}

    public FraudEventDto(String transactionId, Long userId, String accountNumber, BigDecimal amount,
                       String riskLevel, BigDecimal riskScore, String decision, String fraudReasons, LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.riskLevel = riskLevel;
        this.riskScore = riskScore;
        this.decision = decision;
        this.fraudReasons = fraudReasons;
        this.timestamp = timestamp;
    }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public BigDecimal getRiskScore() { return riskScore; }
    public void setRiskScore(BigDecimal riskScore) { this.riskScore = riskScore; }
    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }
    public String getFraudReasons() { return fraudReasons; }
    public void setFraudReasons(String fraudReasons) { this.fraudReasons = fraudReasons; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}