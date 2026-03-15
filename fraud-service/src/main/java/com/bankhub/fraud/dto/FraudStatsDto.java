package com.bankhub.fraud.dto;

public class FraudStatsDto {
    private Long totalChecks;
    private Long approvedTransactions;
    private Long rejectedTransactions;
    private Long flaggedTransactions;
    private Long blockedAccounts;
    private Double fraudDetectionRate;
    private Double falsePositiveRate;
    private Long averageProcessingTimeMs;

    public FraudStatsDto() {}

    public FraudStatsDto(Long totalChecks, Long approvedTransactions, Long rejectedTransactions, Long flaggedTransactions, Long blockedAccounts, Double fraudDetectionRate, Double falsePositiveRate, Long averageProcessingTimeMs) {
        this.totalChecks = totalChecks;
        this.approvedTransactions = approvedTransactions;
        this.rejectedTransactions = rejectedTransactions;
        this.flaggedTransactions = flaggedTransactions;
        this.blockedAccounts = blockedAccounts;
        this.fraudDetectionRate = fraudDetectionRate;
        this.falsePositiveRate = falsePositiveRate;
        this.averageProcessingTimeMs = averageProcessingTimeMs;
    }

    public static FraudStatsDtoBuilder builder() { return new FraudStatsDtoBuilder(); }

    public Long getTotalChecks() { return totalChecks; }
    public void setTotalChecks(Long totalChecks) { this.totalChecks = totalChecks; }
    public Long getApprovedTransactions() { return approvedTransactions; }
    public void setApprovedTransactions(Long approvedTransactions) { this.approvedTransactions = approvedTransactions; }
    public Long getRejectedTransactions() { return rejectedTransactions; }
    public void setRejectedTransactions(Long rejectedTransactions) { this.rejectedTransactions = rejectedTransactions; }
    public Long getFlaggedTransactions() { return flaggedTransactions; }
    public void setFlaggedTransactions(Long flaggedTransactions) { this.flaggedTransactions = flaggedTransactions; }
    public Long getBlockedAccounts() { return blockedAccounts; }
    public void setBlockedAccounts(Long blockedAccounts) { this.blockedAccounts = blockedAccounts; }
    public Double getFraudDetectionRate() { return fraudDetectionRate; }
    public void setFraudDetectionRate(Double fraudDetectionRate) { this.fraudDetectionRate = fraudDetectionRate; }
    public Double getFalsePositiveRate() { return falsePositiveRate; }
    public void setFalsePositiveRate(Double falsePositiveRate) { this.falsePositiveRate = falsePositiveRate; }
    public Long getAverageProcessingTimeMs() { return averageProcessingTimeMs; }
    public void setAverageProcessingTimeMs(Long averageProcessingTimeMs) { this.averageProcessingTimeMs = averageProcessingTimeMs; }

    public static class FraudStatsDtoBuilder {
        private Long totalChecks;
        private Long approvedTransactions;
        private Long rejectedTransactions;
        private Long flaggedTransactions;
        private Long blockedAccounts;
        private Double fraudDetectionRate;
        private Double falsePositiveRate;
        private Long averageProcessingTimeMs;

        FraudStatsDtoBuilder() {}

        public FraudStatsDtoBuilder totalChecks(Long totalChecks) { this.totalChecks = totalChecks; return this; }
        public FraudStatsDtoBuilder approvedTransactions(Long approvedTransactions) { this.approvedTransactions = approvedTransactions; return this; }
        public FraudStatsDtoBuilder rejectedTransactions(Long rejectedTransactions) { this.rejectedTransactions = rejectedTransactions; return this; }
        public FraudStatsDtoBuilder flaggedTransactions(Long flaggedTransactions) { this.flaggedTransactions = flaggedTransactions; return this; }
        public FraudStatsDtoBuilder blockedAccounts(Long blockedAccounts) { this.blockedAccounts = blockedAccounts; return this; }
        public FraudStatsDtoBuilder fraudDetectionRate(Double fraudDetectionRate) { this.fraudDetectionRate = fraudDetectionRate; return this; }
        public FraudStatsDtoBuilder falsePositiveRate(Double falsePositiveRate) { this.falsePositiveRate = falsePositiveRate; return this; }
        public FraudStatsDtoBuilder averageProcessingTimeMs(Long averageProcessingTimeMs) { this.averageProcessingTimeMs = averageProcessingTimeMs; return this; }

        public FraudStatsDto build() {
            return new FraudStatsDto(totalChecks, approvedTransactions, rejectedTransactions, flaggedTransactions, blockedAccounts, fraudDetectionRate, falsePositiveRate, averageProcessingTimeMs);
        }
    }
}