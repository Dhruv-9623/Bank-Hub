package com.bankhub.notification.entity;

public enum NotificationType {
    TRANSACTION_ALERT("Transaction Alert"),
    FRAUD_ALERT("Fraud Alert"),
    BALANCE_UPDATE("Balance Update"),
    ACCOUNT_CREATED("Account Created"),
    LOGIN_ALERT("Login Alert"),
    SECURITY_ALERT("Security Alert"),
    PAYMENT_REMINDER("Payment Reminder"),
    ACCOUNT_LOCKED("Account Locked");

    private final String displayName;

    NotificationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}