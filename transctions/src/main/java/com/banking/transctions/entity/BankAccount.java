package com.banking.transctions.entity;

import com.banking.transctions.entity.enums.TransactionTypes;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "bank_account")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    String customerName;
    BigDecimal debited;
    BigDecimal credited;
    BigDecimal availableBalance;

}