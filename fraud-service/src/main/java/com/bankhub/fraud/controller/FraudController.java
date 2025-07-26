package com.bankhub.fraud.controller;

import com.bankhub.fraud.dto.*;
import com.bankhub.fraud.service.FraudDetectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fraud")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class FraudController {

    private final FraudDetectionService fraudDetectionService;

    @PostMapping("/analyze")
    public ResponseEntity<FraudCheckResponseDto> analyzeTransaction(
            @RequestBody TransactionEventDto transaction) {

        log.info("Fraud analysis request for transaction: {}", transaction.getTransactionId());

        FraudCheckResponseDto result = fraudDetectionService.analyzeTransaction(transaction);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FraudCheckResponseDto>> getUserFraudChecks(
            @PathVariable Long userId) {

        log.info("Fetching fraud checks for user: {}", userId);

        List<FraudCheckResponseDto> checks = fraudDetectionService.getFraudChecksByUser(userId);

        return ResponseEntity.ok(checks);
    }

    @GetMapping("/stats")
    public ResponseEntity<FraudStatsDto> getFraudStatistics() {

        log.info("Fetching fraud detection statistics");

        FraudStatsDto stats = fraudDetectionService.getFraudStatistics();

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("AI Fraud Detection Service is working! 🛡️🤖");
    }
}