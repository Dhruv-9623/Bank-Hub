package com.bankhub.transaction.service;

import com.bankhub.transaction.dto.TransferRequestDto;
import com.bankhub.transaction.entity.Transaction;
import com.bankhub.transaction.entity.TransactionStatus;
import com.bankhub.transaction.entity.TransactionType;
import com.bankhub.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionEventPublisher eventPublisher;

    @Mock
    private AccountServiceClient accountServiceClient;

    @InjectMocks
    private TransactionService transactionService;

    private TransferRequestDto transferRequest;
    private Transaction testTransaction;

    @BeforeEach
    void setUp() {
        transferRequest = new TransferRequestDto();
        transferRequest.setFromAccount("ACC001");
        transferRequest.setToAccount("ACC002");
        transferRequest.setAmount(new BigDecimal("100.00"));
        transferRequest.setDescription("Test transfer");
        transferRequest.setUserId(1L);

        testTransaction = Transaction.builder()
                .id(1L)
                .transactionId("TXN001")
                .fromAccount("ACC001")
                .toAccount("ACC002")
                .amount(new BigDecimal("100.00"))
                .type(TransactionType.TRANSFER)
                .status(TransactionStatus.COMPLETED)
                .userId(1L)
                .description("Test transfer")
                .build();
    }

    @Test
    void transferMoney_Success() {
        when(accountServiceClient.updateBalance(any(), any())).thenReturn(true);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);
        doNothing().when(eventPublisher).publishTransactionEvent(any());

        var result = transactionService.transferMoney(transferRequest);

        assertNotNull(result);
        assertEquals("ACC001", result.getFromAccount());
        assertEquals("ACC002", result.getToAccount());
        assertEquals(TransactionStatus.COMPLETED, result.getStatus());
        verify(eventPublisher).publishTransactionEvent(any());
    }

    @Test
    void getTransactionsByUserId_Success() {
        when(transactionRepository.findByUserIdOrderByCreatedAtDesc(any()))
                .thenReturn(Arrays.asList(testTransaction));

        var results = transactionService.getTransactionsByUserId(1L);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("TXN001", results.get(0).getTransactionId());
    }

    @Test
    void getTransactionsByAccountNumber_Success() {
        when(transactionRepository.findByFromAccountOrToAccountOrderByCreatedAtDesc(any(), any()))
                .thenReturn(Arrays.asList(testTransaction));

        var results = transactionService.getTransactionsByAccountNumber("ACC001");

        assertNotNull(results);
        assertEquals(1, results.size());
    }
}