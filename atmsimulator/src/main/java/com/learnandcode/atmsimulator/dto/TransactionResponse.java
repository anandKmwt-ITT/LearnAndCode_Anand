package com.learnandcode.atmsimulator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionResponse {
    private String message;
    private double accountBalance;
    private double atmBalance;
}
