package com.learnandcode.atmsimulator.service.helper;

import com.learnandcode.atmsimulator.exception.DailyLimitExceededException;
import com.learnandcode.atmsimulator.exception.InsufficientFundsException;
import com.learnandcode.atmsimulator.model.ATM;
import com.learnandcode.atmsimulator.model.UserAccount;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TransactionValidator {

    private final double DAILY_LIMIT = 25000;

    public void validateWithdrawal(UserAccount user, ATM atm, double amount) {
        if (amount > user.getBalance()) {
            throw new InsufficientFundsException("Insufficient funds in account");
        }

        if (amount > atm.getAvailableCash()) {
            throw new InsufficientFundsException("ATM has insufficient cash");
        }

        LocalDate today = LocalDate.now();
        if (!today.equals(user.getLastWithdrawDate())) {
            user.setDailyWithdrawn(0);
            user.setLastWithdrawDate(today);
        }

        if (user.getDailyWithdrawn() + amount > DAILY_LIMIT) {
            throw new DailyLimitExceededException("Daily withdrawal limit exceeded");
        }
    }
}
