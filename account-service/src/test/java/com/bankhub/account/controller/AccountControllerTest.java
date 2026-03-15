package com.bankhub.account.controller;

import com.bankhub.account.dto.AccountCreateDto;
import com.bankhub.account.dto.AccountResponseDto;
import com.bankhub.account.entity.AccountType;
import com.bankhub.account.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createAccount_Success() throws Exception {
        AccountCreateDto dto = new AccountCreateDto();
        dto.setUserId(1L);
        dto.setAccountType(AccountType.CHECKING);
        dto.setInitialDeposit(new BigDecimal("1000.00"));

        AccountResponseDto response = AccountResponseDto.builder()
                .id(1L)
                .accountNumber("ACC1234567890")
                .userId(1L)
                .accountType(AccountType.CHECKING)
                .balance(new BigDecimal("1000.00"))
                .isActive(true)
                .build();

        when(accountService.createAccount(any())).thenReturn(response);

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber").value("ACC1234567890"))
                .andExpect(jsonPath("$.balance").value(1000.00));
    }

    @Test
    void getAccountsByUserId_Success() throws Exception {
        when(accountService.getAccountsByUserId(any())).thenReturn(java.util.Arrays.asList());

        mockMvc.perform(get("/api/accounts/user/1"))
                .andExpect(status().isOk());
    }
}
