package com.bankhub.account.service;

import com.bankhub.account.dto.AccountCreateDto;
import com.bankhub.account.dto.AccountResponseDto;
import com.bankhub.account.dto.BalanceUpdateDto;
import com.bankhub.account.entity.Account;
import com.bankhub.account.entity.AccountType;
import com.bankhub.account.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private AccountCreateDto createDto;
    private Account testAccount;

    @BeforeEach
    void setUp() {
        createDto = new AccountCreateDto();
        createDto.setUserId(1L);
        createDto.setAccountType(AccountType.CHECKING);
        createDto.setInitialDeposit(new BigDecimal("1000.00"));

        testAccount = Account.builder()
                .id(1L)
                .accountNumber("ACC1234567890")
                .userId(1L)
                .accountType(AccountType.CHECKING)
                .balance(new BigDecimal("1000.00"))
                .isActive(true)
                .build();
    }

    @Test
    void createAccount_Success() {
        when(accountRepository.existsByAccountNumber(any())).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        AccountResponseDto result = accountService.createAccount(createDto);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals(AccountType.CHECKING, result.getAccountType());
        assertEquals(new BigDecimal("1000.00"), result.getBalance());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void getAccountsByUserId_Success() {
        when(accountRepository.findByUserIdAndIsActive(any(), eq(true)))
                .thenReturn(Arrays.asList(testAccount));

        var results = accountService.getAccountsByUserId(1L);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("ACC1234567890", results.get(0).getAccountNumber());
    }

    @Test
    void getAccountByNumber_Success() {
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(testAccount));

        AccountResponseDto result = accountService.getAccountByNumber("ACC1234567890");

        assertNotNull(result);
        assertEquals("ACC1234567890", result.getAccountNumber());
    }

    @Test
    void depositMoney_Success() {
        BalanceUpdateDto updateDto = new BalanceUpdateDto();
        updateDto.setAmount(new BigDecimal("500.00"));
        updateDto.setDescription("Test deposit");

        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        AccountResponseDto result = accountService.depositMoney("ACC1234567890", updateDto);

        assertNotNull(result);
        assertEquals(new BigDecimal("1500.00"), result.getBalance());
    }

    @Test
    void withdrawMoney_Success() {
        BalanceUpdateDto updateDto = new BalanceUpdateDto();
        updateDto.setAmount(new BigDecimal("200.00"));
        updateDto.setDescription("Test withdrawal");

        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        AccountResponseDto result = accountService.withdrawMoney("ACC1234567890", updateDto);

        assertNotNull(result);
        assertEquals(new BigDecimal("800.00"), result.getBalance());
    }

    @Test
    void withdrawMoney_InsufficientFunds_ThrowsException() {
        BalanceUpdateDto updateDto = new BalanceUpdateDto();
        updateDto.setAmount(new BigDecimal("2000.00"));

        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(testAccount));

        assertThrows(RuntimeException.class, () ->
                accountService.withdrawMoney("ACC1234567890", updateDto));
    }
}