package com.bankhub.transaction.controller;

import com.bankhub.transaction.dto.TransactionResponseDto;
import com.bankhub.transaction.dto.TransferRequestDto;
import com.bankhub.transaction.entity.TransactionStatus;
import com.bankhub.transaction.entity.TransactionType;
import com.bankhub.transaction.service.TransactionService;
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

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void transferMoney_Success() throws Exception {
        TransferRequestDto dto = new TransferRequestDto();
        dto.setFromAccount("ACC001");
        dto.setToAccount("ACC002");
        dto.setAmount(new BigDecimal("100.00"));
        dto.setDescription("Test transfer");
        dto.setUserId(1L);

        TransactionResponseDto response = TransactionResponseDto.builder()
                .id(1L)
                .transactionId("TXN001")
                .fromAccount("ACC001")
                .toAccount("ACC002")
                .amount(new BigDecimal("100.00"))
                .type(TransactionType.TRANSFER)
                .status(TransactionStatus.COMPLETED)
                .description("Test transfer")
                .build();

        when(transactionService.transferMoney(any())).thenReturn(response);

        mockMvc.perform(post("/api/transactions/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionId").value("TXN001"))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void getTransactionsByUserId_Success() throws Exception {
        when(transactionService.getTransactionsByUserId(any())).thenReturn(java.util.Arrays.asList());

        mockMvc.perform(get("/api/transactions/user/1"))
                .andExpect(status().isOk());
    }
}