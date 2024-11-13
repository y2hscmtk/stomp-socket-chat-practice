package com.choi76.web_socket_jwt.domain.chat.controller;

import com.choi76.web_socket_jwt.domain.chat.service.ChatRoomService;
import com.choi76.web_socket_jwt.domain.chat.service.ChatService;
import com.choi76.web_socket_jwt.global.jwt.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final ChatRoomService chatRoomService;

    /**
     * 채팅방 회원 가입 - 최초 입장
     */
    @PostMapping("/enter/{chatRoomId}")
    public void enterChatRoom(@PathVariable Long chatRoomId,
                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        chatService.enterChatRoom(chatRoomId, userDetails.getEmail());
    }

    /**
     * 채팅방 회원 탈퇴
     */
    @PostMapping("/exit/{chatRoomId}")
    public void exitChatRoom(@PathVariable Long chatRoomId,
                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        chatService.exitChatRoom(chatRoomId, userDetails.getEmail());
    }

    /**
     * 모든 채팅방 정보 반환
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllChatRooms() {
        return chatRoomService.getAllChatRooms();
    }

    /**
     * 채팅방 회원 여부 반환
     */
    @GetMapping("/{chatRoomId}/membership-check")
    public ResponseEntity<?> membershipCheck(@PathVariable Long chatRoomId,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        return chatRoomService.membershipCheck(chatRoomId,userDetails.getEmail());
    }
}

