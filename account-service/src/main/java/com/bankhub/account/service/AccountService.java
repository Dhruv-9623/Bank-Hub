package com.bankhub.account.service;

import com.bankhub.account.dto.*;
import com.bankhub.account.entity.Account;
import com.bankhub.account.entity.AccountType;
import com.bankhub.account.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final Random random = new Random();

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountResponseDto createAccount(AccountCreateDto createDto) {
        // Generate unique account number
        String accountNumber = generateAccountNumber();

        // Create new account
        Account account = Account.builder()
                .accountNumber(accountNumber)
                .userId(createDto.getUserId())
                .accountType(createDto.getAccountType())
                .balance(createDto.getInitialDeposit())
                .isActive(true)
                .lastTransactionDate(LocalDateTime.now())
                .build();

        Account savedAccount = accountRepository.save(account);

        System.out.println("Account created successfully: " + savedAccount.getAccountNumber() + " for user: " + savedAccount.getUserId());

        return mapToAccountResponseDto(savedAccount);
    }

    public List<AccountResponseDto> getAccountsByUserId(Long userId) {
        List<Account> accounts = accountRepository.findByUserIdAndIsActive(userId, true);
        return accounts.stream()
                .map(this::mapToAccountResponseDto)
                .collect(Collectors.toList());
    }

    public AccountResponseDto getAccountByNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));

        return mapToAccountResponseDto(account);
    }

    public AccountResponseDto depositMoney(String accountNumber, BalanceUpdateDto updateDto) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));

        if (!account.getIsActive()) {
            throw new RuntimeException("Account is not active");
        }

        BigDecimal newBalance = account.getBalance().add(updateDto.getAmount());
        account.setBalance(newBalance);
        account.setLastTransactionDate(LocalDateTime.now());

        Account savedAccount = accountRepository.save(account);

        System.out.println("Deposit successful: " + updateDto.getAmount() + " to account: " + accountNumber + ", new balance: " + newBalance);

        return mapToAccountResponseDto(savedAccount);
    }

    public AccountResponseDto withdrawMoney(String accountNumber, BalanceUpdateDto updateDto) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));

        if (!account.getIsActive()) {
            throw new RuntimeException("Account is not active");
        }

        if (account.getBalance().compareTo(updateDto.getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        BigDecimal newBalance = account.getBalance().subtract(updateDto.getAmount());
        account.setBalance(newBalance);
        account.setLastTransactionDate(LocalDateTime.now());

        Account savedAccount = accountRepository.save(account);

        System.out.println("Withdrawal successful: " + updateDto.getAmount() + " from account: " + accountNumber + ", new balance: " + newBalance);

        return mapToAccountResponseDto(savedAccount);
    }

    private String generateAccountNumber() {
        String accountNumber;
        do {
            // Generate 10-digit account number
            accountNumber = "ACC" + String.format("%010d", random.nextInt(1000000000));
        } while (accountRepository.existsByAccountNumber(accountNumber));

        return accountNumber;
    }

    private AccountResponseDto mapToAccountResponseDto(Account account) {
        return AccountResponseDto.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .userId(account.getUserId())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .isActive(account.getIsActive())
                .createdAt(account.getCreatedAt())
                .lastTransactionDate(account.getLastTransactionDate())
                .build();
    }
}