package com.learnandcode.atmsimulator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class UserAccount {

    @Id
    private String cardNumber;

    private String pin;
    private double balance;
    private boolean isBlocked;
    private int invalidPinAttempts;
    private double dailyWithdrawn;
    private LocalDate lastWithdrawDate;
}
