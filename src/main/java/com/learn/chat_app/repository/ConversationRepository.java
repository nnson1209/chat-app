package com.learn.chat_app.repository;

import com.learn.chat_app.entity.Conversation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, String> {

    @EntityGraph(attributePaths = {"participants", "participants.user"})
    Optional<Conversation> findByParticipantHash(String participantHash);

    // Query tất cả conversations mà user tham gia
    // @EntityGraph: Eager load participants và user để tránh N+1 query
    // DISTINCT: Tránh duplicate khi JOIN với participants (1 conversation có nhiều participants)
    // ORDER BY: Sắp xếp theo lastMessageTime giảm dần, conversation có tin nhắn mới nhất lên đầu
    // NULLS LAST: Conversation chưa có tin nhắn (lastMessageTime = null) xuống cuối
    @EntityGraph(attributePaths = {"participants", "participants.user"})
    @Query("SELECT DISTINCT c FROM Conversation c JOIN c.participants p WHERE p.user.id = :userId ORDER BY c.lastMessageTime DESC NULLS LAST")
    Page<Conversation> findAllByUserId(@Param("userId") String userId, Pageable pageable);

    // Thêm method mới để validate user là member của conversation
    // Query: SELECT c FROM Conversation c
    //        WHERE c.id = :conversationId
    //        AND EXISTS (SELECT p FROM c.participants p WHERE p.user.id = :userId)
    @Query("SELECT c FROM Conversation c WHERE c.id = :conversationId AND EXISTS (SELECT p FROM c.participants p WHERE p.user.id = :userId)")
    Optional<Conversation> findByIdAndMember(String conversationId, String userId);

}

