package com.bankhub.fraud.service;

import com.bankhub.fraud.dto.*;
import com.bankhub.fraud.entity.FraudCheck;
import com.bankhub.fraud.entity.FraudDecision;
import com.bankhub.fraud.entity.RiskLevel;
import com.bankhub.fraud.repository.FraudCheckRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FraudDetectionService {

    private final FraudCheckRepository fraudCheckRepository;

    @Value("${fraud.detection.max-transaction-amount:10000.00}")
    private BigDecimal maxTransactionAmount;

    @Value("${fraud.detection.max-daily-transactions:10}")
    private int maxDailyTransactions;

    @Value("${fraud.detection.suspicious-hour-start:0}")
    private int suspiciousHourStart;

    @Value("${fraud.detection.suspicious-hour-end:6}")
    private int suspiciousHourEnd;

    @Value("${fraud.detection.risk-score-threshold:75.0}")
    private double riskScoreThreshold;

    public FraudCheckResponseDto analyzeTransaction(TransactionEventDto transaction) {
        long startTime = System.currentTimeMillis();

        log.info("Starting fraud analysis for transaction: {}", transaction.getTransactionId());

        // AI Risk Analysis
        RiskAnalysisDto riskAnalysis = performRiskAnalysis(transaction);

        // Create fraud check record
        FraudCheck fraudCheck = FraudCheck.builder()
                .transactionId(transaction.getTransactionId())
                .userId(transaction.getUserId())
                .accountNumber(transaction.getFromAccount())
                .amount(transaction.getAmount())
                .riskLevel(mapRiskLevel(riskAnalysis.getRiskScore()))
                .riskScore(riskAnalysis.getRiskScore())
                .decision(mapDecision(riskAnalysis))
                .fraudReasons(String.join(", ", riskAnalysis.getRiskFactors()))
                .processingTimeMs(System.currentTimeMillis() - startTime)
                .build();

        FraudCheck savedCheck = fraudCheckRepository.save(fraudCheck);

        log.info("Fraud analysis completed for transaction: {} - Risk Score: {} - Decision: {}",
                transaction.getTransactionId(), riskAnalysis.getRiskScore(), fraudCheck.getDecision());

        return mapToFraudCheckResponseDto(savedCheck, riskAnalysis);
    }

    private RiskAnalysisDto performRiskAnalysis(TransactionEventDto transaction) {
        List<String> riskFactors = new ArrayList<>();
        BigDecimal riskScore = BigDecimal.ZERO;

        // AI ALGORITHM 1: Amount-based risk scoring
        riskScore = riskScore.add(analyzeTransactionAmount(transaction, riskFactors));

        // AI ALGORITHM 2: Frequency analysis
        riskScore = riskScore.add(analyzeTransactionFrequency(transaction, riskFactors));

        // AI ALGORITHM 3: Time-based analysis
        riskScore = riskScore.add(analyzeTransactionTiming(transaction, riskFactors));

        // AI ALGORITHM 4: Behavioral pattern analysis
        riskScore = riskScore.add(analyzeBehavioralPatterns(transaction, riskFactors));

        // AI ALGORITHM 5: Velocity analysis
        riskScore = riskScore.add(analyzeTransactionVelocity(transaction, riskFactors));

        // Normalize risk score to 0-100 scale
        riskScore = riskScore.min(BigDecimal.valueOf(100));

        boolean isHighRisk = riskScore.doubleValue() >= riskScoreThreshold;
        boolean shouldBlock = riskScore.doubleValue() >= 90.0;

        return RiskAnalysisDto.builder()
                .riskScore(riskScore)
                .riskLevel(mapRiskLevel(riskScore).toString())
                .riskFactors(riskFactors)
                .isHighRisk(isHighRisk)
                .shouldBlock(shouldBlock)
                .decision(shouldBlock ? "BLOCK" : isHighRisk ? "FLAG" : "APPROVE")
                .reasoning(generateReasoning(riskFactors, riskScore))
                .build();
    }

    private BigDecimal analyzeTransactionAmount(TransactionEventDto transaction, List<String> riskFactors) {
        BigDecimal riskScore = BigDecimal.ZERO;

        // Large amount detection
        if (transaction.getAmount().compareTo(maxTransactionAmount) > 0) {
            riskScore = riskScore.add(BigDecimal.valueOf(40));
            riskFactors.add("Transaction amount exceeds maximum limit");
        } else if (transaction.getAmount().compareTo(BigDecimal.valueOf(5000)) > 0) {
            riskScore = riskScore.add(BigDecimal.valueOf(20));
            riskFactors.add("Large transaction amount");
        }

        // Round amount detection (potential money laundering)
        if (transaction.getAmount().remainder(BigDecimal.valueOf(1000)).equals(BigDecimal.ZERO)) {
            riskScore = riskScore.add(BigDecimal.valueOf(15));
            riskFactors.add("Round amount transaction");
        }

        return riskScore;
    }

    private BigDecimal analyzeTransactionFrequency(TransactionEventDto transaction, List<String> riskFactors) {
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        long dailyTransactionCount = fraudCheckRepository.countTransactionsByUserSince(
                transaction.getUserId(), oneDayAgo);

        BigDecimal riskScore = BigDecimal.ZERO;

        if (dailyTransactionCount >= maxDailyTransactions) {
            riskScore = riskScore.add(BigDecimal.valueOf(30));
            riskFactors.add("Excessive daily transaction frequency");
        } else if (dailyTransactionCount >= maxDailyTransactions * 0.8) {
            riskScore = riskScore.add(BigDecimal.valueOf(15));
            riskFactors.add("High daily transaction frequency");
        }

        return riskScore;
    }

    private BigDecimal analyzeTransactionTiming(TransactionEventDto transaction, List<String> riskFactors) {
        LocalTime transactionTime = transaction.getTimestamp().toLocalTime();
        int hour = transactionTime.getHour();

        BigDecimal riskScore = BigDecimal.ZERO;

        // Suspicious hours (late night/early morning)
        if (hour >= suspiciousHourStart && hour <= suspiciousHourEnd) {
            riskScore = riskScore.add(BigDecimal.valueOf(25));
            riskFactors.add("Transaction during suspicious hours");
        }

        return riskScore;
    }

    private BigDecimal analyzeBehavioralPatterns(TransactionEventDto transaction, List<String> riskFactors) {
        BigDecimal riskScore = BigDecimal.ZERO;

        // Get user's recent transaction history
        List<FraudCheck> recentTransactions = fraudCheckRepository.findRecentTransactionsByUser(
                transaction.getUserId()).stream().limit(10).collect(Collectors.toList());

        if (!recentTransactions.isEmpty()) {
            // Analyze amount deviation from user's normal behavior
            BigDecimal avgAmount = recentTransactions.stream()
                    .map(FraudCheck::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(recentTransactions.size()), RoundingMode.HALF_UP);

            BigDecimal deviation = transaction.getAmount().subtract(avgAmount).abs();
            BigDecimal deviationPercentage = deviation.divide(avgAmount.max(BigDecimal.ONE), RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));

            if (deviationPercentage.doubleValue() > 300) {
                riskScore = riskScore.add(BigDecimal.valueOf(35));
                riskFactors.add("Transaction amount significantly deviates from user pattern");
            } else if (deviationPercentage.doubleValue() > 150) {
                riskScore = riskScore.add(BigDecimal.valueOf(20));
                riskFactors.add("Transaction amount moderately deviates from user pattern");
            }
        }

        return riskScore;
    }

    private BigDecimal analyzeTransactionVelocity(TransactionEventDto transaction, List<String> riskFactors) {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        BigDecimal hourlyAmount = fraudCheckRepository.sumTransactionAmountByUserSince(
                transaction.getUserId(), oneHourAgo);

        BigDecimal riskScore = BigDecimal.ZERO;

        if (hourlyAmount != null && hourlyAmount.compareTo(BigDecimal.valueOf(20000)) > 0) {
            riskScore = riskScore.add(BigDecimal.valueOf(40));
            riskFactors.add("High transaction velocity - large amounts in short time");
        } else if (hourlyAmount != null && hourlyAmount.compareTo(BigDecimal.valueOf(10000)) > 0) {
            riskScore = riskScore.add(BigDecimal.valueOf(25));
            riskFactors.add("Moderate transaction velocity");
        }

        return riskScore;
    }

    private RiskLevel mapRiskLevel(BigDecimal riskScore) {
        double score = riskScore.doubleValue();
        if (score >= 90) return RiskLevel.CRITICAL;
        if (score >= 75) return RiskLevel.HIGH;
        if (score >= 40) return RiskLevel.MEDIUM;
        return RiskLevel.LOW;
    }

    private FraudDecision mapDecision(RiskAnalysisDto riskAnalysis) {
        if (riskAnalysis.isShouldBlock()) return FraudDecision.REJECTED;
        if (riskAnalysis.isHighRisk()) return FraudDecision.FLAGGED;
        return FraudDecision.APPROVED;
    }

    private String generateReasoning(List<String> riskFactors, BigDecimal riskScore) {
        if (riskFactors.isEmpty()) {
            return "Transaction appears normal with low risk indicators";
        }
        return "Risk Score: " + riskScore + "%. Factors: " + String.join("; ", riskFactors);
    }

    private FraudCheckResponseDto mapToFraudCheckResponseDto(FraudCheck fraudCheck, RiskAnalysisDto riskAnalysis) {
        return FraudCheckResponseDto.builder()
                .id(fraudCheck.getId())
                .transactionId(fraudCheck.getTransactionId())
                .userId(fraudCheck.getUserId())
                .accountNumber(fraudCheck.getAccountNumber())
                .amount(fraudCheck.getAmount())
                .riskLevel(fraudCheck.getRiskLevel())
                .riskScore(fraudCheck.getRiskScore())
                .decision(fraudCheck.getDecision())
                .fraudReasons(List.of(fraudCheck.getFraudReasons().split(", ")))
                .processingTimeMs(fraudCheck.getProcessingTimeMs())
                .createdAt(fraudCheck.getCreatedAt())
                .isBlocked(fraudCheck.getDecision() == FraudDecision.REJECTED)
                .recommendedAction(generateRecommendedAction(fraudCheck.getDecision()))
                .build();
    }

    private String generateRecommendedAction(FraudDecision decision) {
        return switch (decision) {
            case APPROVED -> "Process transaction normally";
            case FLAGGED -> "Flag for manual review";
            case REJECTED -> "Block transaction and notify user";
            case BLOCKED -> "Block account and escalate to security team";
            default -> "Pending analysis";
        };
    }

    public List<FraudCheckResponseDto> getFraudChecksByUser(Long userId) {
        return fraudCheckRepository.findByUserId(userId).stream()
                .map(check -> mapToFraudCheckResponseDto(check, null))
                .collect(Collectors.toList());
    }

    public FraudStatsDto getFraudStatistics() {
        long totalChecks = fraudCheckRepository.count();
        long approved = fraudCheckRepository.findByDecision(FraudDecision.APPROVED).size();
        long rejected = fraudCheckRepository.findByDecision(FraudDecision.REJECTED).size();
        long flagged = fraudCheckRepository.findByDecision(FraudDecision.FLAGGED).size();
        long blocked = fraudCheckRepository.findByDecision(FraudDecision.BLOCKED).size();

        double fraudDetectionRate = totalChecks > 0 ? (double) (rejected + flagged) / totalChecks * 100 : 0;
        double falsePositiveRate = totalChecks > 0 ? (double) flagged / totalChecks * 100 : 0;
        Double avgProcessingTime = fraudCheckRepository.getAverageProcessingTime();

        return FraudStatsDto.builder()
                .totalChecks(totalChecks)
                .approvedTransactions(approved)
                .rejectedTransactions(rejected)
                .flaggedTransactions(flagged)
                .blockedAccounts(blocked)
                .fraudDetectionRate(fraudDetectionRate)
                .falsePositiveRate(falsePositiveRate)
                .averageProcessingTimeMs(avgProcessingTime != null ? avgProcessingTime.longValue() : 0L)
                .build();
    }
}