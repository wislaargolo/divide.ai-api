package com.ufrn.imd.divide.ai.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Where;

import java.io.Serializable;

@Entity
@Table(name = "payments")
@Where(clause = "active = true")
public class Payment extends BaseEntity {

    @Column(nullable = false)
    private Double amount;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
