package com.learnandcode.atmsimulator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ATM {

    @Id
    private int id = 1;
    private double availableCash;
}
