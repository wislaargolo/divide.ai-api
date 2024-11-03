package com.ufrn.imd.divide.ai.repository;

import com.ufrn.imd.divide.ai.model.GroupTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupTransactionRepository extends JpaRepository<GroupTransaction, Long> {
}
