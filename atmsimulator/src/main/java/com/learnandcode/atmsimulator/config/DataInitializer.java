package com.learnandcode.atmsimulator.config;

import com.learnandcode.atmsimulator.model.ATM;
import com.learnandcode.atmsimulator.model.UserAccount;
import com.learnandcode.atmsimulator.repository.ATMRepository;
import com.learnandcode.atmsimulator.repository.UserAccountRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UserAccountRepository userRepository;
    private final ATMRepository atmRepository;

    @PostConstruct
    public void init() {
        initDefaultUserAccount();
        initATM();
    }

    private void initDefaultUserAccount() {
        String defaultCardNumber = "1234567890123456";
        if (userRepository.findById(defaultCardNumber).isEmpty()) {
            UserAccount user = new UserAccount();
            user.setCardNumber(defaultCardNumber);
            user.setPin("1234");
            user.setBalance(10000);
            user.setBlocked(false);
            user.setInvalidPinAttempts(0);
            user.setDailyWithdrawn(0);
            user.setLastWithdrawDate(LocalDate.now().minusDays(1));
            userRepository.save(user);
        }
    }

    private void initATM() {
        if (atmRepository.findById(1).isEmpty()) {
            ATM atm = new ATM();
            atm.setId(1);
            atm.setAvailableCash(100000);
            atmRepository.save(atm);
        }
    }
}
