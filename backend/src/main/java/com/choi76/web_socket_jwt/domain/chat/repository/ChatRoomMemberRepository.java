package com.choi76.web_socket_jwt.domain.chat.repository;

import com.choi76.web_socket_jwt.domain.chat.entity.ChatRoom;
import com.choi76.web_socket_jwt.domain.chat.entity.ChatRoomMember;
import com.choi76.web_socket_jwt.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    Optional<ChatRoomMember> findByChatRoomIdAndMemberId(Long chatRoomId, Long memberId);

    @Query("SELECT cm FROM ChatRoomMember cm WHERE cm.chatRoom = :chatRoom AND cm.member = :member")
    Optional<ChatRoomMember> findByChatRoomAndMember(@Param("chatRoom") ChatRoom chatRoom, @Param("member") Member member);

}
