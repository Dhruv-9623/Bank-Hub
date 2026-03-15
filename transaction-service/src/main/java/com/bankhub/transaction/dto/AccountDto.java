package com.bankhub.transaction.dto;

import java.math.BigDecimal;

public class AccountDto {
    private Long id;
    private String accountNumber;
    private Long userId;
    private String accountType;
    private BigDecimal balance;
    private Boolean isActive;

    public AccountDto() {}

    public AccountDto(Long id, String accountNumber, Long userId, String accountType, BigDecimal balance, Boolean isActive) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.accountType = accountType;
        this.balance = balance;
        this.isActive = isActive;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}