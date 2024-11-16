package com.ufrn.imd.divide.ai.repository;

import com.ufrn.imd.divide.ai.dto.response.UserTransactionByMonthResponseDTO;
import com.ufrn.imd.divide.ai.model.User;
import com.ufrn.imd.divide.ai.model.UserTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserTransactionRepository extends JpaRepository<UserTransaction, Long> {
    List<UserTransaction> findAllByUser(User user);
}
