package com.ufrn.imd.divide.ai.repository;

import com.ufrn.imd.divide.ai.model.Debt;
import com.ufrn.imd.divide.ai.model.Group;
import com.ufrn.imd.divide.ai.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Long> {

    boolean existsByUserAndGroupTransactionGroupAndPaidAtIsNull(User user, Group group);

    List<Debt> findByGroupTransaction_Id(Long groupTransactionId);

    @Query("SELECT d FROM Debt d WHERE d.groupTransaction.id = :groupTransactionId " +
            "ORDER BY CASE WHEN d.paidAt IS NOT NULL THEN 0 ELSE 1 END, d.paidAt DESC, d.createdAt DESC")
    List<Debt> findByGroupTransactionIdOrderByPaidAtDescThenCreatedAtDesc(@Param("groupTransactionId") Long groupTransactionId);

}
