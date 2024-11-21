package com.ufrn.imd.divide.ai.repository;

import com.ufrn.imd.divide.ai.model.VWUserTransactionsGroupedByMonth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VWUserTransactionsGroupedByMonthRepository extends JpaRepository<VWUserTransactionsGroupedByMonth, VWUserTransactionsGroupedByMonth.PK>{
    List<VWUserTransactionsGroupedByMonth> findByUserId(Long userId);
}
