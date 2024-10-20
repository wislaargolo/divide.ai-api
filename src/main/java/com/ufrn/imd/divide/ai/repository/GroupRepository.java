package com.ufrn.imd.divide.ai.repository;

import com.ufrn.imd.divide.ai.model.Group;
import com.ufrn.imd.divide.ai.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    boolean existsByCode(String code);
    List<Group> findByMembers_Id(Long userId);
    Optional<Group> findByCode(String code);
    Optional<Group> findByNameAndCreatedBy(String name, User createdBy);
}
