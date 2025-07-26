package com.bankhub.account.entity;

public enum AccountType {
    CHECKING("Checking Account"),
    SAVINGS("Savings Account"),
    CREDIT_CARD("Credit Card Account"),
    BUSINESS_CHECKING("Business Checking"),
    MONEY_MARKET("Money Market Account");

    private final String displayName;

    AccountType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}