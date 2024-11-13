package com.choi76.web_socket_jwt.domain.chat.service;

import com.choi76.web_socket_jwt.domain.chat.dto.ChatMessageResponseDTO;
import com.choi76.web_socket_jwt.domain.chat.entity.ChatMessage;
import com.choi76.web_socket_jwt.domain.chat.entity.ChatRoom;
import com.choi76.web_socket_jwt.domain.chat.entity.ChatRoomDto;
import com.choi76.web_socket_jwt.domain.chat.entity.ChatRoomMember;
import com.choi76.web_socket_jwt.domain.chat.repository.ChatMessageRepository;
import com.choi76.web_socket_jwt.domain.chat.repository.ChatRoomMemberRepository;
import com.choi76.web_socket_jwt.domain.chat.repository.ChatRoomRepository;
import com.choi76.web_socket_jwt.domain.member.entity.Member;
import com.choi76.web_socket_jwt.domain.member.repository.MemberRepository;
import com.choi76.web_socket_jwt.global.enums.statuscode.ErrorStatus;
import com.choi76.web_socket_jwt.global.exception.GeneralException;
import com.choi76.web_socket_jwt.global.response.ApiResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatMessageRepository chatMessageRepository;
    public ResponseEntity<?> getAllChatRooms() {
        List<ChatRoom> allChatRooms = chatRoomRepository.findAll();
        ArrayList<ChatRoomDto> resultDto = new ArrayList<>();
        for (ChatRoom allChatRoom : allChatRooms) {
            resultDto.add(ChatRoomDto.builder()
                    .chatRoomId(allChatRoom.getId())
                    .chatRoomName(allChatRoom.getName())
                    .build());
        }
        return ResponseEntity.ok(ApiResponse.onSuccess(resultDto));
    }

    // 모든 채팅 반환
    public ResponseEntity<?> getAllChats(Long chatRoomId) {
        ChatRoom chatRoom = findChatRoomById(chatRoomId);
        List<ChatMessage> byChatRoom = chatMessageRepository.findByChatRoom(chatRoom);

        ArrayList<ChatMessageResponseDTO> resultDto = new ArrayList<>();
        // 반환 DTO 생성
        for (ChatMessage chatMessage : byChatRoom) {
            resultDto.add(ChatMessageResponseDTO.builder()
                    .messageType(chatMessage.getMessageType())
                    .content(chatMessage.getContent())
                    .email(chatMessage.getSender().getEmail())
                    .build());
        }
        return ResponseEntity.ok(ApiResponse.onSuccess(resultDto));
    }


    // 채팅방 회원 여부 확인
    public ResponseEntity<?> membershipCheck(Long chatRoomId, String email) {
        Member member = findMemberByEmail(email);
        ChatRoom chatRoom = findChatRoomById(chatRoomId);

        Optional<ChatRoomMember> chatRoomMember = chatRoomMemberRepository
                .findByChatRoomAndMember(chatRoom,
                member);
        // 회원 여부 반환
        return ResponseEntity.ok(ApiResponse.onSuccess(chatRoomMember.isPresent()));
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
    }

    public ChatRoom findChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHAT_ROOM_NOT_FOUND));
    }
}
