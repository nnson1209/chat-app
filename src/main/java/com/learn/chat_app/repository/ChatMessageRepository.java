package com.learn.chat_app.repository;

import com.learn.chat_app.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {

    // Query messages by conversationId với pagination
    // @EntityGraph: Eager load sender để tránh N+1 query
    // mediaFiles (JSON column) không cần fetch, tự động load
    @EntityGraph(attributePaths = {"sender"})
    Page<ChatMessage> findByConversationId(String conversationId, Pageable pageable);
}
