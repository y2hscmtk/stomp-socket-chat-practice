package com.choi76.web_socket_jwt.global.socket;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Web socket session 및 데이터 저장
 * 연결된 토픽 관리
 */
@Slf4j
@Component
public class SocketSessionRegistry {
	private final ConcurrentMap<String, Set<Long>> sessionSubscriptions = new ConcurrentHashMap<>(); //<loginId, set<ChatRoomId>>

	/**
	 * 구독한 토픽 데이터 추가
	 */
	public void addSubscription(String loginId, Long chatRoomId) {
		sessionSubscriptions.computeIfAbsent(loginId, key -> ConcurrentHashMap.newKeySet()).add(chatRoomId);
	}

	/**
	 * 구독한 특정 토픽 데이터 제거
	 */
	public void removeSubscription(String loginId, Long chatRoomId) {
		Set<Long> subscriptions = sessionSubscriptions.get(loginId);
		if (subscriptions != null) {
			subscriptions.remove(chatRoomId);
			if (subscriptions.isEmpty()) {
				sessionSubscriptions.remove(loginId);
			}
		}
	}

	/**
	 * 현재 구독중인 토픽 데이터
	 */
	public Set<Long> getSubscriptions(String loginId) {
		return sessionSubscriptions.get(loginId);
	}

	/**
	 * 구독된 토픽 모두 제거
	 */
	public void removeSession(String loginId) {
		sessionSubscriptions.remove(loginId);
	}
}
