package com.bankhub.account.dto;

import com.bankhub.account.entity.AccountType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class AccountCreateDto {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Account type is required")
    private AccountType accountType;

    private BigDecimal initialDeposit = BigDecimal.ZERO;

    public AccountCreateDto() {}

    public AccountCreateDto(Long userId, AccountType accountType, BigDecimal initialDeposit) {
        this.userId = userId;
        this.accountType = accountType;
        this.initialDeposit = initialDeposit;
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

    public BigDecimal getInitialDeposit() {
        return initialDeposit;
    }

    public void setInitialDeposit(BigDecimal initialDeposit) {
        this.initialDeposit = initialDeposit;
    }
}