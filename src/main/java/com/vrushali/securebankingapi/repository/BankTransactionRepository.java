package com.vrushali.securebankingapi.repository;

import com.vrushali.securebankingapi.entity.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankTransactionRepository
        extends JpaRepository<BankTransaction, Long> {

    List<BankTransaction> findBySourceAccountAccountNumberOrderByCreatedAtDesc(
            String accountNumber
    );

    List<BankTransaction> findByDestinationAccountAccountNumberOrderByCreatedAtDesc(
            String accountNumber
    );
}