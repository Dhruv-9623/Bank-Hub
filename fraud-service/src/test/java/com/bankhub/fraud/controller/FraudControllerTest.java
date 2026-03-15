package com.bankhub.fraud.controller;

import com.bankhub.fraud.dto.FraudCheckResponseDto;
import com.bankhub.fraud.dto.TransactionEventDto;
import com.bankhub.fraud.entity.FraudDecision;
import com.bankhub.fraud.entity.RiskLevel;
import com.bankhub.fraud.service.FraudDetectionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FraudController.class)
class FraudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FraudDetectionService fraudDetectionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void analyzeTransaction_Success() throws Exception {
        TransactionEventDto dto = new TransactionEventDto();
        dto.setTransactionId("TXN001");
        dto.setUserId(1L);
        dto.setFromAccount("ACC001");
        dto.setToAccount("ACC002");
        dto.setAmount(new BigDecimal("100.00"));
        dto.setTransactionType("TRANSFER");
        dto.setTimestamp(LocalDateTime.now());

        FraudCheckResponseDto response = FraudCheckResponseDto.builder()
                .id(1L)
                .transactionId("TXN001")
                .userId(1L)
                .riskScore(25.0)
                .riskLevel(RiskLevel.LOW)
                .decision(FraudDecision.APPROVED)
                .build();

        when(fraudDetectionService.analyzeTransaction(any())).thenReturn(response);

        mockMvc.perform(post("/api/fraud/analyze")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.decision").value("APPROVED"))
                .andExpect(jsonPath("$.riskLevel").value("LOW"));
    }

    @Test
    void getFraudStats_Success() throws Exception {
        when(fraudDetectionService.getFraudStatistics()).thenReturn(
                com.bankhub.fraud.dto.FraudStatsDto.builder()
                        .totalTransactionsAnalyzed(100L)
                        .blockedTransactions(5L)
                        .highRiskTransactions(10L)
                        .build()
        );

        mockMvc.perform(get("/api/fraud/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalTransactionsAnalyzed").value(100));
    }
}