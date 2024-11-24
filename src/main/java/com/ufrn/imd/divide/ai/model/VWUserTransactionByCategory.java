package com.ufrn.imd.divide.ai.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import org.hibernate.annotations.Immutable;

import java.io.Serializable;

// Copiar o script SQL abaixo para criar a view no banco
/*
    CREATE VIEW vw_user_transactions_by_category as SELECT
        c.id AS category_id,
        c.name AS category_name,
        ut.user_id,
        SUM(ut.amount) AS amount,
        EXTRACT(MONTH FROM ut.created_at) AS month,
        EXTRACT(YEAR FROM ut.created_at) AS year,
		c.color
    FROM
        user_transactions ut
    JOIN
        categories c ON ut.category_id = c.id
    GROUP BY
        c.id, c.name, ut.user_id, EXTRACT(MONTH FROM ut.created_at), EXTRACT(YEAR FROM ut.created_at)
    ORDER BY
        year, month, category_name;

* */
@Entity(name = "vw_user_transactions_by_category")
@Immutable
@IdClass(VWUserTransactionByCategory.PK.class)
public class VWUserTransactionByCategory {
    @Id
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name")
    private String categoryName;
    @Column(name = "amount")
    private double amount;
    @Id
    @Column(name = "month")
    private int month;
    @Id
    @Column(name = "year")
    private int year;
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "color")
    private String color;

    public static class PK implements Serializable {
        private Long categoryId;

        private int month;

        private int year;
        private Long userId;

        public PK() {
        }

        public PK(Long categoryId, int month, int year, Long userId) {
            this.categoryId = categoryId;
            this.month = month;
            this.year = year;
            this.userId = userId;
        }

        public Long getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Long categoryId) {
            this.categoryId = categoryId;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }
    }

    public VWUserTransactionByCategory(Long categoryId, String categoryName, double amount, int month, int year, Long userId, String color) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.amount = amount;
        this.month = month;
        this.year = year;
        this.userId = userId;
        this.color = color;
    }

    public VWUserTransactionByCategory() {
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
