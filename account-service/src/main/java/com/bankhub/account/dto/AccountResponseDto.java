package com.bankhub.account.dto;

import com.bankhub.account.entity.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountResponseDto {
    private Long id;
    private String accountNumber;
    private Long userId;
    private AccountType accountType;
    private BigDecimal balance;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime lastTransactionDate;

    public AccountResponseDto() {}

    public AccountResponseDto(Long id, String accountNumber, Long userId, AccountType accountType, BigDecimal balance, Boolean isActive, LocalDateTime createdAt, LocalDateTime lastTransactionDate) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.accountType = accountType;
        this.balance = balance;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.lastTransactionDate = lastTransactionDate;
    }

    public static AccountResponseBuilder builder() {
        return new AccountResponseBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastTransactionDate() {
        return lastTransactionDate;
    }

    public void setLastTransactionDate(LocalDateTime lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }

    public static class AccountResponseBuilder {
        private Long id;
        private String accountNumber;
        private Long userId;
        private AccountType accountType;
        private BigDecimal balance;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime lastTransactionDate;

        AccountResponseBuilder() {}

        public AccountResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AccountResponseBuilder accountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public AccountResponseBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public AccountResponseBuilder accountType(AccountType accountType) {
            this.accountType = accountType;
            return this;
        }

        public AccountResponseBuilder balance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public AccountResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public AccountResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public AccountResponseBuilder lastTransactionDate(LocalDateTime lastTransactionDate) {
            this.lastTransactionDate = lastTransactionDate;
            return this;
        }

        public AccountResponseDto build() {
            return new AccountResponseDto(id, accountNumber, userId, accountType, balance, isActive, createdAt, lastTransactionDate);
        }
    }
}