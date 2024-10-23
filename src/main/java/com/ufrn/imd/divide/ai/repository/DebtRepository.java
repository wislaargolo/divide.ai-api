package com.ufrn.imd.divide.ai.repository;

import com.ufrn.imd.divide.ai.model.Debt;
import com.ufrn.imd.divide.ai.model.Group;
import com.ufrn.imd.divide.ai.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Long> {

    boolean existsByUserAndGroupTransactionGroupAndPaidAtIsNull(User user, Group group);

}
