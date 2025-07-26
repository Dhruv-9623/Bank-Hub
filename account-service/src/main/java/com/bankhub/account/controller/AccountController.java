package com.bankhub.account.controller;

import com.bankhub.account.dto.*;
import com.bankhub.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@Validated
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AccountResponseDto> createAccount(
            @Valid @RequestBody AccountCreateDto createDto) {

        log.info("Account creation request for user: {}, type: {}",
                createDto.getUserId(), createDto.getAccountType());

        AccountResponseDto account = accountService.createAccount(createDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountResponseDto>> getAccountsByUser(
            @PathVariable Long userId) {

        log.info("Fetching accounts for user: {}", userId);

        List<AccountResponseDto> accounts = accountService.getAccountsByUserId(userId);

        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponseDto> getAccountByNumber(
            @PathVariable String accountNumber) {

        log.info("Fetching account details for: {}", accountNumber);

        AccountResponseDto account = accountService.getAccountByNumber(accountNumber);

        return ResponseEntity.ok(account);
    }

    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<AccountResponseDto> depositMoney(
            @PathVariable String accountNumber,
            @Valid @RequestBody BalanceUpdateDto updateDto) {

        log.info("Deposit request: {} to account: {}",
                updateDto.getAmount(), accountNumber);

        AccountResponseDto account = accountService.depositMoney(accountNumber, updateDto);

        return ResponseEntity.ok(account);
    }

    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<AccountResponseDto> withdrawMoney(
            @PathVariable String accountNumber,
            @Valid @RequestBody BalanceUpdateDto updateDto) {

        log.info("Withdrawal request: {} from account: {}",
                updateDto.getAmount(), accountNumber);

        AccountResponseDto account = accountService.withdrawMoney(accountNumber, updateDto);

        return ResponseEntity.ok(account);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Account Service is working! 🏦");
    }
}