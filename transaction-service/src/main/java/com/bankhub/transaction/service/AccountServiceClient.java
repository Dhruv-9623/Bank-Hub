package com.bankhub.transaction.service;

import com.bankhub.transaction.dto.AccountDto;
import com.bankhub.transaction.dto.BalanceUpdateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "account-service")
public interface AccountServiceClient {

    @GetMapping("/api/accounts/{accountNumber}")
    AccountDto getAccountByNumber(@PathVariable("accountNumber") String accountNumber);

    @PostMapping("/api/accounts/{accountNumber}/withdraw")
    AccountDto withdrawMoney(@PathVariable("accountNumber") String accountNumber,
                             @RequestBody BalanceUpdateDto updateDto);

    @PostMapping("/api/accounts/{accountNumber}/deposit")
    AccountDto depositMoney(@PathVariable("accountNumber") String accountNumber,
                            @RequestBody BalanceUpdateDto updateDto);
}