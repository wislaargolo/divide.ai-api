package com.ufrn.imd.divide.ai.repository;

import com.ufrn.imd.divide.ai.model.UserTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTransactionRepository extends JpaRepository<UserTransaction, Long> {

}
