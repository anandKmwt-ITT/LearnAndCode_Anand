package com.learnandcode.rest.repository;

import com.learnandcode.rest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public boolean existsByPhoneNumber(String phoneNumber);
    public boolean existsByEmail(String email);
}