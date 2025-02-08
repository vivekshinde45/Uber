package com.project.uber.dto;

import com.project.uber.entities.enums.TransactionMethod;
import com.project.uber.entities.enums.TransactionType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WalletTransactionDto {
    private Long id;
    private Double balance;
    private TransactionType transactionType;
    private TransactionMethod transactionMethod;
    private RideDto ride;
    private String transactionId;
    private WalletDto wallet;
    private LocalDateTime timeStamp;
}
