package com.bankhub.transaction.repository;

import com.bankhub.transaction.entity.Transaction;
import com.bankhub.transaction.entity.TransactionStatus;
import com.bankhub.transaction.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserId(Long userId);

    List<Transaction> findByFromAccountOrToAccount(String fromAccount, String toAccount);

    Optional<Transaction> findByTransactionId(String transactionId);

    List<Transaction> findByStatus(TransactionStatus status);

    List<Transaction> findByTransactionType(TransactionType transactionType);

    @Query("SELECT t FROM Transaction t WHERE t.userId = ?1 ORDER BY t.createdAt DESC")
    List<Transaction> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT t FROM Transaction t WHERE (t.fromAccount = ?1 OR t.toAccount = ?1) AND t.createdAt BETWEEN ?2 AND ?3")
    List<Transaction> findByAccountAndDateRange(String accountNumber, LocalDateTime startDate, LocalDateTime endDate);

    boolean existsByTransactionId(String transactionId);
}