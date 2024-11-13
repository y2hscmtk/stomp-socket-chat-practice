package com.choi76.web_socket_jwt.domain.chat.repository;

import com.choi76.web_socket_jwt.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
