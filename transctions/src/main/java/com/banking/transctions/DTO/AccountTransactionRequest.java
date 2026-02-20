package com.banking.transctions.DTO;

import com.banking.transctions.entity.enums.TransactionTypes;
import jakarta.validation.constraints.Min;
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
public class AccountTransactionRequest {
    private UUID id;
    private TransactionTypes transactionTypes;
    @Min(0)
    private BigDecimal amount;
}
