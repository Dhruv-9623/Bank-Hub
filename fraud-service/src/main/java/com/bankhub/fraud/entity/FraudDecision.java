package com.bankhub.fraud.entity;

public enum FraudDecision {
    PENDING("Pending Review"),
    APPROVED("Transaction Approved"),
    REJECTED("Transaction Rejected - Fraud Detected"),
    FLAGGED("Transaction Flagged for Manual Review"),
    BLOCKED("Account Blocked - Suspicious Activity");

    private final String description;

    FraudDecision(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}