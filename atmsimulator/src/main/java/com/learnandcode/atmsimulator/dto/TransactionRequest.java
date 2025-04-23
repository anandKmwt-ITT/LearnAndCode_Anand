package com.learnandcode.atmsimulator.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionRequest {
    @NotBlank
    private String cardNumber;

    @NotBlank
    private String pin;

    @Min(1)
    private double amount;
}
