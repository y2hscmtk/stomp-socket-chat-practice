package com.choi76.web_socket_jwt.domain.chat.controller;

import com.choi76.web_socket_jwt.domain.chat.dto.ChatMessageRequestDTO;
import com.choi76.web_socket_jwt.domain.chat.service.ChatService;
import com.choi76.web_socket_jwt.global.jwt.util.JwtUtil;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatService chatService;
    private final JwtUtil jwtUtil;

    // Client 요청 에시
    // stompClient.send(`/app/chat.sendMessage`, { 'Authorization': jwtToken }, JSON.stringify(chatMessage));
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageRequestDTO messageDTO, SimpMessageHeaderAccessor headerAccessor) {
        String email = jwtUtil.getEmailFromJWT(headerAccessor.getFirstNativeHeader("Authorization"));
        chatService.sendMessage(messageDTO, email);

    }

    @MessageMapping("/chat.enter")
    public void enterChatRoom(@Payload ChatMessageRequestDTO messageDTO, SimpMessageHeaderAccessor headerAccessor) {
        String email = jwtUtil.getEmailFromJWT(headerAccessor.getFirstNativeHeader("Authorization"));
        chatService.enterChatRoom(messageDTO.getChatRoomId(), email);
    }

    @MessageMapping("/chat.exit")
    public void exitChatRoom(@Payload ChatMessageRequestDTO messageDTO, SimpMessageHeaderAccessor headerAccessor) {
        String email = jwtUtil.getEmailFromJWT(headerAccessor.getFirstNativeHeader("Authorization"));
        chatService.exitChatRoom(messageDTO.getChatRoomId(), email);
    }
}

