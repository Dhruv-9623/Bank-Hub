package com.bankhub.fraud.service;

import com.bankhub.fraud.dto.TransactionEventDto;
import com.bankhub.fraud.entity.FraudCheck;
import com.bankhub.fraud.entity.FraudDecision;
import com.bankhub.fraud.entity.RiskLevel;
import com.bankhub.fraud.repository.FraudCheckRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FraudDetectionServiceTest {

    @Mock
    private FraudCheckRepository fraudCheckRepository;

    @InjectMocks
    private FraudDetectionService fraudDetectionService;

    private TransactionEventDto transactionEvent;

    @BeforeEach
    void setUp() {
        transactionEvent = new TransactionEventDto();
        transactionEvent.setTransactionId("TXN001");
        transactionEvent.setUserId(1L);
        transactionEvent.setFromAccount("ACC001");
        transactionEvent.setToAccount("ACC002");
        transactionEvent.setAmount(new BigDecimal("100.00"));
        transactionEvent.setTransactionType("TRANSFER");
        transactionEvent.setTimestamp(LocalDateTime.now());
        transactionEvent.setIpAddress("192.168.1.1");
        transactionEvent.setDeviceId("DEVICE001");
        transactionEvent.setLocation("Toronto");
    }

    @Test
    void analyzeTransaction_LowRisk() {
        transactionEvent.setAmount(new BigDecimal("50.00"));

        when(fraudCheckRepository.save(any(FraudCheck.class))).thenAnswer(i -> i.getArgument(0));

        var result = fraudDetectionService.analyzeTransaction(transactionEvent);

        assertNotNull(result);
        assertEquals(FraudDecision.APPROVED, result.getDecision());
        assertEquals(RiskLevel.LOW, result.getRiskLevel());
    }

    @Test
    void analyzeTransaction_HighRisk_LargeAmount() {
        transactionEvent.setAmount(new BigDecimal("15000.00"));

        when(fraudCheckRepository.save(any(FraudCheck.class))).thenAnswer(i -> i.getArgument(0));

        var result = fraudDetectionService.analyzeTransaction(transactionEvent);

        assertNotNull(result);
        assertEquals(FraudDecision.BLOCKED, result.getDecision());
        assertEquals(RiskLevel.HIGH, result.getRiskLevel());
    }

    @Test
    void analyzeTransaction_MediumRisk_UnusualHour() {
        transactionEvent.setAmount(new BigDecimal("500.00"));
        transactionEvent.setTimestamp(LocalDateTime.now().withHour(3));

        when(fraudCheckRepository.save(any(FraudCheck.class))).thenAnswer(i -> i.getArgument(0));

        var result = fraudDetectionService.analyzeTransaction(transactionEvent);

        assertNotNull(result);
        assertEquals(RiskLevel.MEDIUM, result.getRiskLevel());
    }

    @Test
    void getFraudStatistics_Success() {
        when(fraudCheckRepository.count()).thenReturn(100L);
        when(fraudCheckRepository.countByDecision(any())).thenReturn(5L);
        when(fraudCheckRepository.countByRiskLevel(any())).thenReturn(10L);

        var result = fraudDetectionService.getFraudStatistics();

        assertNotNull(result);
        assertEquals(100L, result.getTotalTransactionsAnalyzed());
    }
}