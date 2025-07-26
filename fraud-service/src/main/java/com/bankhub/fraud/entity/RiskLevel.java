package com.bankhub.fraud.entity;

public enum RiskLevel {
    LOW("Low Risk - Safe Transaction"),
    MEDIUM("Medium Risk - Monitor Closely"),
    HIGH("High Risk - Requires Review"),
    CRITICAL("Critical Risk - Block Transaction");

    private final String description;

    RiskLevel(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}