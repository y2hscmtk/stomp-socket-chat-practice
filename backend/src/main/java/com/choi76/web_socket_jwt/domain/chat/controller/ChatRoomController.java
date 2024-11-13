package com.choi76.web_socket_jwt.domain.chat.controller;

import com.choi76.web_socket_jwt.domain.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/chatroom")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @GetMapping("/all/{chatRoomId}")
    public ResponseEntity<?> getAllChats(@PathVariable("chatRoomId") Long chatRoomId) {
        return chatRoomService.getAllChats(chatRoomId);
    }
}
