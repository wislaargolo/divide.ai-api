package com.ufrn.imd.divide.ai.repository;

import com.ufrn.imd.divide.ai.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OpenAIRepository extends JpaRepository<Chat, Long> {
    @Query(value = "SELECT c FROM Chat c WHERE c.userId = ?1 ORDER BY c.createdAt DESC LIMIT 1", nativeQuery = true)
    Chat findLatestChatByUserId(Long userId);
}
