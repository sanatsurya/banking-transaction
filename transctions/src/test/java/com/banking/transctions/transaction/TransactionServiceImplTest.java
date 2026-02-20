package com.banking.transctions.transaction;

import com.banking.transctions.DTO.AccountBalanceResponse;
import com.banking.transctions.DTO.AccountTransactionRequest;
import com.banking.transctions.DTO.AccountTransactionResponse;
import com.banking.transctions.entity.BankAccount;
import com.banking.transctions.entity.enums.TransactionTypes;
import com.banking.transctions.repository.BankAccountRepository;
import com.banking.transctions.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TransactionServiceImplTest {

    @Mock
    private BankAccountRepository repository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private TransactionServiceImpl service;

    private UUID accountId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountId = UUID.randomUUID();
    }

    @Test
    void testGetAvailableBalance_Success() {
        BankAccount account = BankAccount.builder()
                .id(accountId)
                .availableBalance(BigDecimal.valueOf(1000))
                .build();

        AccountBalanceResponse response = AccountBalanceResponse.builder()
                .id(accountId)
                .availableBalance(BigDecimal.valueOf(1000))
                .build();

        when(repository.findById(accountId)).thenReturn(Optional.of(account));
        when(mapper.map(Optional.of(account), AccountBalanceResponse.class)).thenReturn(response);

        AccountBalanceResponse result = service.getAvailableBalance(accountId);

        assertEquals(accountId, result.getId());
        assertEquals(BigDecimal.valueOf(1000), result.getAvailableBalance());
    }

    @Test
    void testUpdateBalance_DebitSuccess() {
        BankAccount account = BankAccount.builder()
                .id(accountId)
                .availableBalance(BigDecimal.valueOf(1000))
                .build();

        AccountTransactionRequest request = AccountTransactionRequest.builder()
                .id(accountId)
                .transactionTypes(TransactionTypes.DEBIT)
                .amount(BigDecimal.valueOf(200))
                .build();

        when(repository.findById(accountId)).thenReturn(Optional.of(account));
        when(repository.save(any(BankAccount.class))).thenReturn(account);

        AccountTransactionResponse response = AccountTransactionResponse.builder()
                .id(accountId)
                .transactionTypes(TransactionTypes.DEBIT)
                .availableBalance(BigDecimal.valueOf(800))
                .build();

        when(mapper.map(account, AccountTransactionResponse.class)).thenReturn(response);

        AccountTransactionResponse result = service.updateBalance(request);

        assertEquals(TransactionTypes.DEBIT, result.getTransactionTypes());
        assertEquals(BigDecimal.valueOf(800), result.getAvailableBalance());
    }

    @Test
    void testUpdateBalance_DebitInsufficientBalance() {
        BankAccount account = BankAccount.builder()
                .id(accountId)
                .availableBalance(BigDecimal.valueOf(1000))
                .build();

        AccountTransactionRequest request = AccountTransactionRequest.builder()
                .id(accountId)
                .transactionTypes(TransactionTypes.DEBIT)
                .amount(BigDecimal.valueOf(2000))
                .build();

        when(repository.findById(accountId)).thenReturn(Optional.of(account));

        assertThrows(IllegalArgumentException.class, () -> service.updateBalance(request));
    }

    @Test
    void testUpdateBalance_CreditSuccess() {
        BankAccount account = BankAccount.builder()
                .id(accountId)
                .availableBalance(BigDecimal.valueOf(1000))
                .build();

        AccountTransactionRequest request = AccountTransactionRequest.builder()
                .id(accountId)
                .transactionTypes(TransactionTypes.CREDIT)
                .amount(BigDecimal.valueOf(300))
                .build();

        when(repository.findById(accountId)).thenReturn(Optional.of(account));
        when(repository.save(any(BankAccount.class))).thenReturn(account);

        AccountTransactionResponse response = AccountTransactionResponse.builder()
                .id(accountId)
                .transactionTypes(TransactionTypes.CREDIT)
                .availableBalance(BigDecimal.valueOf(1300))
                .build();

        when(mapper.map(account, AccountTransactionResponse.class)).thenReturn(response);

        AccountTransactionResponse result = service.updateBalance(request);

        assertEquals(TransactionTypes.CREDIT, result.getTransactionTypes());
        assertEquals(BigDecimal.valueOf(1300), result.getAvailableBalance());
    }

    @Test
    void testUpdateBalance_InvalidAccount() {
        AccountTransactionRequest request = AccountTransactionRequest.builder()
                .id(accountId)
                .transactionTypes(TransactionTypes.CREDIT)
                .amount(BigDecimal.valueOf(100))
                .build();

        when(repository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.updateBalance(request));
    }
}