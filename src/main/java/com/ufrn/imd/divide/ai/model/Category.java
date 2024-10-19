package com.ufrn.imd.divide.ai.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "categories")
@Where(clause = "active = true")
public class Category extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column
    private String color;

    @Column
    private Boolean isExpense;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getExpense() {
        return isExpense;
    }

    public void setExpense(Boolean expense) {
        isExpense = expense;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}