package com.bankhub.account.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", unique = true, nullable = false)
    private String accountNumber;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @Column(precision = 19, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_transaction_date")
    private LocalDateTime lastTransactionDate;

    @Version
    private Long version;

    public Account() {}

    public Account(String accountNumber, Long userId, AccountType accountType, BigDecimal balance, Boolean isActive, LocalDateTime createdAt, LocalDateTime lastTransactionDate) {
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.accountType = accountType;
        this.balance = balance;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.lastTransactionDate = lastTransactionDate;
    }

    public static AccountBuilder builder() {
        return new AccountBuilder();
    }

    // Getters and Setters
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    // Builder class
    public static class AccountBuilder {
        private Long id;
        private String accountNumber;
        private Long userId;
        private AccountType accountType;
        private BigDecimal balance = BigDecimal.ZERO;
        private Boolean isActive = true;
        private LocalDateTime createdAt;
        private LocalDateTime lastTransactionDate;

        AccountBuilder() {}

        public AccountBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AccountBuilder accountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public AccountBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public AccountBuilder accountType(AccountType accountType) {
            this.accountType = accountType;
            return this;
        }

        public AccountBuilder balance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public AccountBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public AccountBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public AccountBuilder lastTransactionDate(LocalDateTime lastTransactionDate) {
            this.lastTransactionDate = lastTransactionDate;
            return this;
        }

        public Account build() {
            Account account = new Account();
            account.id = this.id;
            account.accountNumber = this.accountNumber;
            account.userId = this.userId;
            account.accountType = this.accountType;
            account.balance = this.balance;
            account.isActive = this.isActive;
            account.createdAt = this.createdAt;
            account.lastTransactionDate = this.lastTransactionDate;
            return account;
        }
    }
}