package com.vrushali.securebankingapi.service;

import com.vrushali.securebankingapi.dto.TransactionRequest;
import com.vrushali.securebankingapi.dto.TransactionResponse;
import com.vrushali.securebankingapi.dto.TransferRequest;
import com.vrushali.securebankingapi.entity.*;
import com.vrushali.securebankingapi.exception.AccountNotFoundException;
import com.vrushali.securebankingapi.exception.InsufficientBalanceException;
import com.vrushali.securebankingapi.exception.InvalidTransactionException;
import com.vrushali.securebankingapi.repository.BankAccountRepository;
import com.vrushali.securebankingapi.repository.BankTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final BankAccountRepository accountRepository;
    private final BankTransactionRepository transactionRepository;

    @Transactional
    public TransactionResponse deposit(TransactionRequest request) {

        validateAmount(request.getAmount());

        BankAccount account = getActiveAccountForUpdate(
                request.getAccountNumber()
        );

        account.setBalance(
                account.getBalance().add(request.getAmount())
        );

        accountRepository.save(account);

        BankTransaction transaction = BankTransaction.builder()
                .referenceNumber(generateReferenceNumber())
                .destinationAccount(account)
                .amount(request.getAmount())
                .type(TransactionType.DEPOSIT)
                .status(TransactionStatus.SUCCESS)
                .description(request.getDescription())
                .build();

        return mapToResponse(transactionRepository.save(transaction));
    }

    @Transactional
    public TransactionResponse withdraw(TransactionRequest request) {

        validateAmount(request.getAmount());

        BankAccount account = getActiveAccountForUpdate(
                request.getAccountNumber()
        );

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient account balance"
            );
        }

        account.setBalance(
                account.getBalance().subtract(request.getAmount())
        );

        accountRepository.save(account);

        BankTransaction transaction = BankTransaction.builder()
                .referenceNumber(generateReferenceNumber())
                .sourceAccount(account)
                .amount(request.getAmount())
                .type(TransactionType.WITHDRAWAL)
                .status(TransactionStatus.SUCCESS)
                .description(request.getDescription())
                .build();

        return mapToResponse(transactionRepository.save(transaction));
    }

    @Transactional
    public TransactionResponse transfer(TransferRequest request) {

        validateAmount(request.getAmount());

        if (request.getSourceAccountNumber()
                .equals(request.getDestinationAccountNumber())) {

            throw new InvalidTransactionException(
                    "Source and destination accounts cannot be the same"
            );
        }

        BankAccount sourceAccount = getActiveAccountForUpdate(
                request.getSourceAccountNumber()
        );

        BankAccount destinationAccount = getActiveAccountForUpdate(
                request.getDestinationAccountNumber()
        );

        if (sourceAccount.getBalance()
                .compareTo(request.getAmount()) < 0) {

            throw new InsufficientBalanceException(
                    "Insufficient account balance"
            );
        }

        sourceAccount.setBalance(
                sourceAccount.getBalance().subtract(request.getAmount())
        );

        destinationAccount.setBalance(
                destinationAccount.getBalance().add(request.getAmount())
        );

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        BankTransaction transaction = BankTransaction.builder()
                .referenceNumber(generateReferenceNumber())
                .sourceAccount(sourceAccount)
                .destinationAccount(destinationAccount)
                .amount(request.getAmount())
                .type(TransactionType.TRANSFER)
                .status(TransactionStatus.SUCCESS)
                .description(request.getDescription())
                .build();

        return mapToResponse(transactionRepository.save(transaction));
    }

    private BankAccount getActiveAccountForUpdate(String accountNumber) {

        BankAccount account = accountRepository
                .findByAccountNumberForUpdate(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Account not found: " + accountNumber
                ));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new InvalidTransactionException(
                    "Account is not active"
            );
        }

        return account;
    }

    private void validateAmount(BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException(
                    "Transaction amount must be greater than zero"
            );
        }
    }

    private String generateReferenceNumber() {
        return "TXN-" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .toUpperCase();
    }

    private TransactionResponse mapToResponse(
            BankTransaction transaction
    ) {

        return TransactionResponse.builder()
                .transactionId(transaction.getId())
                .referenceNumber(transaction.getReferenceNumber())
                .sourceAccountNumber(
                        transaction.getSourceAccount() == null
                                ? null
                                : transaction.getSourceAccount()
                                .getAccountNumber()
                )
                .destinationAccountNumber(
                        transaction.getDestinationAccount() == null
                                ? null
                                : transaction.getDestinationAccount()
                                .getAccountNumber()
                )
                .amount(transaction.getAmount())
                .type(transaction.getType().name())
                .status(transaction.getStatus().name())
                .description(transaction.getDescription())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}