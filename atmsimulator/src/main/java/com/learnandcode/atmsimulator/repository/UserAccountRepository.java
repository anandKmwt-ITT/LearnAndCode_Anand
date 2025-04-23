package com.learnandcode.atmsimulator.repository;

import com.learnandcode.atmsimulator.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
}
