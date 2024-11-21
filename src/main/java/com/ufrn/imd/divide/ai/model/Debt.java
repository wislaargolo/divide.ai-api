package com.ufrn.imd.divide.ai.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "debts")
@SQLRestriction("active = true")
public class Debt extends BaseEntity {

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    @JsonBackReference
    private GroupTransaction groupTransaction;

    @Column(nullable = false)
    private Double amount;

    private LocalDateTime paidAt;

    public Debt() {
    }

    public Debt(Long id) {
        super(id);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GroupTransaction getGroupTransaction() {
        return groupTransaction;
    }

    public void setGroupTransaction(GroupTransaction groupTransaction) {
        this.groupTransaction = groupTransaction;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }
}
