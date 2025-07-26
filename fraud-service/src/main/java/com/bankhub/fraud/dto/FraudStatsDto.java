package com.bankhub.fraud.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FraudStatsDto {
    private Long totalChecks;
    private Long approvedTransactions;
    private Long rejectedTransactions;
    private Long flaggedTransactions;
    private Long blockedAccounts;
    private Double fraudDetectionRate;
    private Double falsePositiveRate;
    private Long averageProcessingTimeMs;
}