package com.bankhub.account.repository;

import com.bankhub.account.entity.Account;
import com.bankhub.account.entity.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByUserId(Long userId);

    List<Account> findByUserIdAndIsActive(Long userId, Boolean isActive);

    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findByAccountType(AccountType accountType);

    boolean existsByAccountNumber(String accountNumber);

    @Query("SELECT COUNT(a) FROM Account a WHERE a.userId = ?1")
    long countByUserId(Long userId);
}