package com.bankhub.notification.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
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
}