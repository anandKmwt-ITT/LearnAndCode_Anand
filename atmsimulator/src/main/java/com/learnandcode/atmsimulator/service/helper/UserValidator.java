package com.learnandcode.atmsimulator.service.helper;

import com.learnandcode.atmsimulator.exception.CardBlockedException;
import com.learnandcode.atmsimulator.model.UserAccount;
import com.learnandcode.atmsimulator.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserAccountRepository userRepository;

    public UserAccount validateUserAndPin(String cardNumber, String pin) {
        UserAccount user = userRepository.findById(cardNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isBlocked()) {
            throw new CardBlockedException("Card is blocked due to 3 invalid attempts.");
        }

        if (!user.getPin().equals(pin)) {
            int attempts = user.getInvalidPinAttempts() + 1;
            user.setInvalidPinAttempts(attempts);
            if (attempts >= 3) {
                user.setBlocked(true);
                userRepository.save(user);
                throw new CardBlockedException("Card blocked after 3 invalid attempts.");
            }
            userRepository.save(user);
            throw new RuntimeException("Invalid PIN. Attempt " + attempts + "/3");
        }

        user.setInvalidPinAttempts(0);
        return user;
    }
}
