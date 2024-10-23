package com.ufrn.imd.divide.ai.repository;

import com.ufrn.imd.divide.ai.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCaseAndActiveTrue(String email);
    Optional<User> findByPhoneNumberAndActiveTrue(String phoneNumber);
    Optional<User> findByIdAndActiveTrue(Long id);
}
