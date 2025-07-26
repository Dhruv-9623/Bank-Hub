package com.bankhub.fraud.repository;

import com.bankhub.fraud.entity.FraudCheck;
import com.bankhub.fraud.entity.FraudDecision;
import com.bankhub.fraud.entity.RiskLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FraudCheckRepository extends JpaRepository<FraudCheck, Long> {

    Optional<FraudCheck> findByTransactionId(String transactionId);

    List<FraudCheck> findByUserId(Long userId);

    List<FraudCheck> findByAccountNumber(String accountNumber);

    List<FraudCheck> findByRiskLevel(RiskLevel riskLevel);

    List<FraudCheck> findByDecision(FraudDecision decision);

    @Query("SELECT COUNT(f) FROM FraudCheck f WHERE f.userId = ?1 AND f.createdAt >= ?2")
    long countTransactionsByUserSince(Long userId, LocalDateTime since);

    @Query("SELECT SUM(f.amount) FROM FraudCheck f WHERE f.userId = ?1 AND f.createdAt >= ?2")
    BigDecimal sumTransactionAmountByUserSince(Long userId, LocalDateTime since);

    @Query("SELECT f FROM FraudCheck f WHERE f.userId = ?1 ORDER BY f.createdAt DESC")
    List<FraudCheck> findRecentTransactionsByUser(Long userId);

    @Query("SELECT AVG(f.processingTimeMs) FROM FraudCheck f")
    Double getAverageProcessingTime();

    @Query("SELECT COUNT(f) FROM FraudCheck f WHERE f.decision = 'REJECTED' AND f.createdAt >= ?1")
    long countRejectedTransactionsSince(LocalDateTime since);
}