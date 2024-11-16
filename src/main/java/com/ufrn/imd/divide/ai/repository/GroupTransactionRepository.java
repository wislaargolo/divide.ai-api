package com.ufrn.imd.divide.ai.repository;

import com.ufrn.imd.divide.ai.model.GroupTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GroupTransactionRepository extends JpaRepository<GroupTransaction, Long> {
  // List<GroupTransaction> findByGroupIdAndActiveTrue(Long groupId);
  List<GroupTransaction> findByGroupId(Long groupId);
  List<GroupTransaction> findByDueDate(LocalDate dueDate);
}
