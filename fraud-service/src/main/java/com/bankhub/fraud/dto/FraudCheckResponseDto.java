package com.bankhub.fraud.dto;

import com.bankhub.fraud.entity.FraudDecision;
import com.bankhub.fraud.entity.RiskLevel;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
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
}