package com.choi76.web_socket_jwt.domain.chat.repository;

import com.choi76.web_socket_jwt.domain.chat.entity.ChatMessage;
import com.choi76.web_socket_jwt.domain.chat.entity.ChatRoom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoom(ChatRoom chatRoom);
}

