package com.ufrn.imd.divide.ai.repository;

import com.ufrn.imd.divide.ai.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByName(String name);
    List<Category> findByUserId(Long userId);
    @Query("SELECT c FROM Category c WHERE LOWER(c.name) = LOWER(:name) AND c.user.id = :userId AND c.expense = :expense")
    List<Category> findByNameIgnoreCaseAndUserIdAndExpense(@Param("name") String name, @Param("userId") Long userId, @Param("expense") Boolean expense);

    List<Category> findByUserIdAndExpense(Long userId, boolean expense);
    List<Category> findByNameContainingIgnoreCase(String name);
}