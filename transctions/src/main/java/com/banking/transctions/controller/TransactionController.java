package com.banking.transctions.controller;

import com.banking.transctions.DTO.AccountBalanceResponse;
import com.banking.transctions.DTO.AccountTransactionRequest;
import com.banking.transctions.DTO.AccountTransactionResponse;
import com.banking.transctions.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/wallet")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService service;
    @GetMapping("/{id}")
    public ResponseEntity<AccountBalanceResponse> getAccountBalance(@PathVariable("id") UUID id){
        return ResponseEntity.ok(service.getAvailableBalance(id));
    }
    @PostMapping
    public ResponseEntity<AccountTransactionResponse> updateBalance(@RequestBody AccountTransactionRequest accountTransactionRequest){
        return ResponseEntity.ok(service.updateBalance(accountTransactionRequest));
    }
}
