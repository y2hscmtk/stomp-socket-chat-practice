package com.choi76.web_socket_jwt.domain.chat.service;

import com.choi76.web_socket_jwt.domain.chat.dto.ChatMessageRequestDTO;
import com.choi76.web_socket_jwt.domain.chat.dto.ChatMessageResponseDTO;
import com.choi76.web_socket_jwt.domain.chat.entity.ChatMessage;
import com.choi76.web_socket_jwt.domain.chat.entity.ChatRoom;
import com.choi76.web_socket_jwt.domain.chat.entity.ChatRoomMember;
import com.choi76.web_socket_jwt.domain.chat.entity.enums.MessageType;
import com.choi76.web_socket_jwt.domain.chat.repository.ChatMessageRepository;
import com.choi76.web_socket_jwt.domain.chat.repository.ChatRoomRepository;
import com.choi76.web_socket_jwt.domain.chat.repository.ChatRoomMemberRepository;
import com.choi76.web_socket_jwt.domain.member.entity.Member;
import com.choi76.web_socket_jwt.domain.member.repository.MemberRepository;
import com.choi76.web_socket_jwt.global.enums.statuscode.ErrorStatus;
import com.choi76.web_socket_jwt.global.exception.GeneralException;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final MessageType ANNOUNCE_MESSAGE = MessageType.ANNOUNCE;
    private final MessageType TALK_MESSAGE = MessageType.TALK;

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;
    private final SimpMessagingTemplate messagingTemplate;



    @Transactional
    public void sendMessage(ChatMessageRequestDTO messageDTO, String email) {
        ChatRoom chatRoom = findChatRoomById(messageDTO.getChatRoomId());
        Member sender = findMemberByEmail(email);

        // 저장용 메시지 생성
        ChatMessage chatMessage =
                toChatMessage(messageDTO.getContent(), TALK_MESSAGE, chatRoom, sender);
        chatMessageRepository.save(chatMessage);

        // 반환용 메시지 생성
        ChatMessageResponseDTO responseDto =
                toChatMessageResponseDto(messageDTO.getContent(), TALK_MESSAGE, sender);

        // topic/chatroom/{chatRoomId} 를 구독한 Client 들에게 새로운 데이터 전송
        messagingTemplate.convertAndSend("/topic/chatroom/" + chatRoom.getId(), responseDto);
    }

    @Transactional
    public void enterChatRoom(Long chatRoomId, String email) {
        ChatRoom chatRoom = findChatRoomById(chatRoomId);
        Member sender = findMemberByEmail(email);

        Optional<ChatRoomMember> membership =
                chatRoomMemberRepository.findByChatRoomAndMember(chatRoom, sender);

        // 이미 가입된 회원 인지 검사
        if (membership.isPresent()) {
            throw new GeneralException(ErrorStatus.CHAT_ROOM_MEMBER_ALREADY_EXISTS);
        }

        // 채팅방 멤버 생성
        ChatRoomMember chatRoomMember = ChatRoomMember.builder()
                .chatRoom(chatRoom)
                .member(sender)
                .build();

        chatRoomMemberRepository.save(chatRoomMember);

        // 채팅방 입장 메시지 생성
        String content = sender.getEmail() + " 님이 입장하셨습니다.";
        ChatMessage enterMessage =
                toChatMessage(content, ANNOUNCE_MESSAGE, chatRoom, sender);

        // 채팅방 반환용 메시지
        ChatMessageResponseDTO responseDto =
                toChatMessageResponseDto(content,ANNOUNCE_MESSAGE, sender);
        chatMessageRepository.save(enterMessage);

        messagingTemplate.convertAndSend("/topic/chatroom/" + chatRoomId, responseDto);
    }

    @Transactional
    public void exitChatRoom(Long chatRoomId, String email) {
        Member sender = findMemberByEmail(email);
        ChatRoom chatRoom = findChatRoomById(chatRoomId);

        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByChatRoomAndMember(chatRoom,sender)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHAT_ROOM_MEMBER_NOT_FOUND));

        // 채팅방 회원 제거
        chatRoomMemberRepository.delete(chatRoomMember);

        // 채팅방 퇴장 메시지 생성 및 저장
        String content = sender.getEmail() + " 님이 퇴장하셨습니다.";
        ChatMessage exitMessage =
                toChatMessage(content, ANNOUNCE_MESSAGE, chatRoom, sender);
        chatMessageRepository.save(exitMessage);

        // 채팅방 반환용 메시지
        ChatMessageResponseDTO responseDto =
                toChatMessageResponseDto(content, ANNOUNCE_MESSAGE, sender);

        messagingTemplate.convertAndSend("/topic/chatroom/" + chatRoomId, responseDto);
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findMemberByEmail(email)
                         .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
    }

    public ChatRoom findChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHAT_ROOM_NOT_FOUND));
    }

    public ChatMessage toChatMessage(String content, MessageType messageType,
                                     ChatRoom chatRoom, Member sender) {
        return ChatMessage.builder()
                .content(content)
                .timestamp(LocalDateTime.now())
                .chatRoom(chatRoom)
                .sender(sender)
                .messageType(messageType)
                .build();
    }

    public ChatMessageResponseDTO toChatMessageResponseDto(
            String message, MessageType messageType, Member sender) {
        return ChatMessageResponseDTO.builder()
                .messageType(messageType)
                .email(sender.getEmail())
                .content(message)
                .build();
    }
}
