package com.example.genie.common.component;

import com.example.genie.domain.chat.service.ChatService;
import com.example.genie.domain.user.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 팟(채팅방)별로 세션을 분리해 메시지를 격리하는 WebSocket 핸들러.
 * 단일 인스턴스(싱글톤)가 potId별 세션 그룹을 관리한다.
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class WebSocketComponent extends TextWebSocketHandler {

    private static final String ATTR_AUTH = "authentication";
    private static final String ATTR_POT_ID = "potId";

    private final UserService userService;
    private final ChatService chatService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // potId -> (sessionId -> session)
    private final Map<Long, Map<String, WebSocketSession>> rooms = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // potId는 클라이언트의 첫 메시지에서 확인되므로 여기서는 인증 정보만 보관
        session.getAttributes().put(ATTR_AUTH, session.getPrincipal());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        JsonNode node;
        try {
            node = objectMapper.readTree(payload);
        } catch (IOException e) {
            log.warn("Invalid chat payload: {}", payload);
            return;
        }

        Authentication authentication = (Authentication) session.getAttributes().get(ATTR_AUTH);
        if (authentication == null) {
            log.warn("Authentication not found for WebSocket session");
            session.close(CloseStatus.NOT_ACCEPTABLE);
            return;
        }

        String type = node.path("type").asText("");
        switch (type) {
            case "chatRoomInfo":
                joinRoom(session, authentication, node.path("potId").asLong());
                break;
            case "chat":
                handleChat(session, authentication, node.path("potId").asLong(), payload);
                break;
            default:
                // join/exit 등은 서버가 방 입장/퇴장 시점에 직접 브로드캐스트한다
                break;
        }
    }

    private void joinRoom(WebSocketSession session, Authentication authentication, long potId) throws IOException {
        if (potId <= 0) {
            return;
        }
        if (!chatService.isMember(authentication, potId)) {
            session.sendMessage(new TextMessage(systemMessage("exit", "채팅 권한이 없습니다.")));
            session.close(CloseStatus.NOT_ACCEPTABLE);
            return;
        }
        bindSession(session, potId);

        // 해당 팟의 이전 메시지를 입장한 세션에만 전송
        for (String stored : chatService.getChatMessages(potId)) {
            session.sendMessage(new TextMessage(stored));
        }

        String username = userService.getUserNickName(authentication);
        broadcast(potId, systemMessage("join", username));
    }

    private void handleChat(WebSocketSession session, Authentication authentication, long potId, String payload) throws IOException {
        if (potId <= 0) {
            return;
        }
        Long bound = (Long) session.getAttributes().get(ATTR_POT_ID);
        if (bound == null) {
            // chatRoomInfo 없이 바로 들어온 경우에도 권한 확인 후 바인딩
            if (!chatService.isMember(authentication, potId)) {
                session.close(CloseStatus.NOT_ACCEPTABLE);
                return;
            }
            bindSession(session, potId);
        } else if (bound != potId) {
            // 바인딩된 방과 다른 potId로의 전송은 차단
            return;
        }

        try {
            chatService.saveChatMessage(authentication, payload, potId);
        } catch (Exception e) {
            log.error("Failed to save chat message", e);
        }
        broadcast(potId, payload);
    }

    private void bindSession(WebSocketSession session, long potId) {
        session.getAttributes().put(ATTR_POT_ID, potId);
        rooms.computeIfAbsent(potId, key -> new ConcurrentHashMap<>()).put(session.getId(), session);
    }

    private void broadcast(long potId, String message) {
        Map<String, WebSocketSession> room = rooms.get(potId);
        if (room == null) {
            return;
        }
        for (WebSocketSession session : room.values()) {
            try {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(message));
                }
            } catch (IOException e) {
                log.error("Failed to send message to WebSocket session", e);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Object potIdAttr = session.getAttributes().get(ATTR_POT_ID);
        if (potIdAttr == null) {
            return;
        }
        long potId = (Long) potIdAttr;

        Map<String, WebSocketSession> room = rooms.get(potId);
        if (room != null) {
            room.remove(session.getId());
            if (room.isEmpty()) {
                rooms.remove(potId);
            }
        }

        Authentication authentication = (Authentication) session.getAttributes().get(ATTR_AUTH);
        if (authentication != null) {
            String username = userService.getUserNickName(authentication);
            broadcast(potId, systemMessage("exit", username));
        }
    }

    private String systemMessage(String type, String username) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("type", type);
        node.put("username", username);
        return node.toString();
    }
}
