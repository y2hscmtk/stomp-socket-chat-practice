package com.choi76.web_socket_jwt.domain.chat.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatRoomDto {
    private Long chatRoomId;
    private String chatRoomName;
}
