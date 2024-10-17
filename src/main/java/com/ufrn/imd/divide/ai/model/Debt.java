package com.ufrn.imd.divide.ai.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "debts")
@Where(clause = "active = true")
public class Debt extends BaseEntity {

    // talvez deixar lazy
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JsonBackReference
    private GroupTransaction groupTransaction;

    @Column(nullable = false)
    private Double amount;

    @OneToOne
    private Payment payment;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public GroupTransaction getGroupTransaction() {
        return groupTransaction;
    }

    public void setGroupTransaction(GroupTransaction groupTransaction) {
        this.groupTransaction = groupTransaction;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
