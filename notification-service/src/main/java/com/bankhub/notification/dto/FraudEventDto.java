package com.bankhub.notification.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
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
}