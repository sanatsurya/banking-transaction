package com.banking.transctions.service;

import com.banking.transctions.DTO.AccountBalanceResponse;
import com.banking.transctions.DTO.AccountTransactionRequest;
import com.banking.transctions.DTO.AccountTransactionResponse;
import com.banking.transctions.entity.enums.TransactionTypes;

import java.math.BigDecimal;
import java.util.UUID;

public interface TransactionService {
    AccountBalanceResponse getAvailableBalance(UUID id);
    AccountTransactionResponse updateBalance(AccountTransactionRequest transactionRequest);
}
