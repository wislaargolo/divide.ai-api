package com.ufrn.imd.divide.ai.repository;

import com.ufrn.imd.divide.ai.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByName(String name);
    List<Category> findByNameContainingIgnoreCase(String name);
}