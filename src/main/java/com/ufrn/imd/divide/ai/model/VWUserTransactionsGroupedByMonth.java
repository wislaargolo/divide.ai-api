package com.ufrn.imd.divide.ai.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import org.hibernate.annotations.Immutable;

import java.io.Serializable;


// Copiar o script SQL abaixo para criar a view no banco
/*
    CREATE VIEW vw_user_transactions_grouped_by_month as SELECT
        EXTRACT(YEAR FROM created_at) AS year,
        EXTRACT(MONTH FROM created_at) AS month,
        SUM(CASE WHEN amount > 0 THEN amount ELSE 0 END) AS total_income,
        SUM(CASE WHEN amount < 0 THEN amount ELSE 0 END) AS total_expenses,
        user_id
    FROM
        user_transactions
    GROUP BY
        EXTRACT(YEAR FROM created_at), EXTRACT(MONTH FROM created_at), user_id
    ORDER BY
        year, month;
*/
@Entity(name = "vw_user_transactions_grouped_by_month")
@Immutable
@IdClass(VWUserTransactionsGroupedByMonth.PK.class)
public class VWUserTransactionsGroupedByMonth {
    @Id
    @Column(name = "month")
    private int month;
    @Id
    @Column(name = "year")
    private int year;
    @Column(name = "total_income")
    private double totalIncome;
    @Column(name = "total_expenses")
    private double totalExpenses;
    @Id
    @Column(name = "user_id")
    private Long userId;

    public static class PK implements Serializable {
        private int month;
        private int year;
        private Long userId;

        public PK() {
        }

        public PK(int month, int year, Long userId) {
            this.month = month;
            this.year = year;
            this.userId = userId;
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

    public VWUserTransactionsGroupedByMonth() {
    }

    public VWUserTransactionsGroupedByMonth(int month, int year, double totalIncome, double totalExpenses, Long userId) {
        this.month = month;
        this.year = year;
        this.totalIncome = totalIncome;
        this.totalExpenses = totalExpenses;
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }
}
