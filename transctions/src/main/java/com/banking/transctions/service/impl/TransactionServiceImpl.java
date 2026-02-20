package com.banking.transctions.service.impl;

import com.banking.transctions.DTO.AccountBalanceResponse;
import com.banking.transctions.DTO.AccountTransactionRequest;
import com.banking.transctions.DTO.AccountTransactionResponse;
import com.banking.transctions.entity.BankAccount;
import com.banking.transctions.entity.enums.TransactionTypes;
import com.banking.transctions.repository.BankAccountRepository;
import com.banking.transctions.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl implements TransactionService {
    private final BankAccountRepository repository;
    private final ModelMapper mapper;
    @Override
    public AccountBalanceResponse getAvailableBalance(UUID id) {
        Optional<BankAccount> account = repository.findById(id);
        return mapper.map(account,AccountBalanceResponse.class);
    }

    @Override
    public AccountTransactionResponse updateBalance(AccountTransactionRequest transactionRequest) {
        BankAccount account = repository.findById(transactionRequest.getId())
                .orElseThrow(() -> new IllegalArgumentException("Account not valid"));
        if (transactionRequest.getTransactionTypes() == TransactionTypes.DEBIT) {
            if (transactionRequest.getAmount().compareTo(BigDecimal.ZERO) > 0
                    && account.getAvailableBalance().compareTo(transactionRequest.getAmount()) >= 0) {
                account.setDebited(transactionRequest.getAmount());
                account.setCredited(BigDecimal.ZERO);
                account.setAvailableBalance(account.getAvailableBalance().subtract(transactionRequest.getAmount()));
            } else {
                throw new IllegalArgumentException("Insufficient balance or invalid amount");
            }
        } else if (transactionRequest.getTransactionTypes() == TransactionTypes.CREDIT) {
            if (transactionRequest.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                account.setCredited(transactionRequest.getAmount());
                account.setDebited(BigDecimal.ZERO);
                account.setAvailableBalance(account.getAvailableBalance().add(transactionRequest.getAmount()));
            } else {
                throw new IllegalArgumentException("Invalid deposit amount");
            }
        }

        BankAccount updatedAccount = repository.save(account);
        return mapper.map(updatedAccount, AccountTransactionResponse.class);
    }
}
