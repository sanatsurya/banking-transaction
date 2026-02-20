package com.banking.transctions.DTO;

import com.banking.transctions.entity.enums.TransactionTypes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankDTO {
    private UUID id;
    private String customerName;
    private TransactionTypes transactionTypes;
    private BigDecimal debited;
    private BigDecimal credited;
    private BigDecimal availableBalance;
}
