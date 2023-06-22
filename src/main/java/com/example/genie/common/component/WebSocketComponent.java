package com.example.genie.common.component;

import com.example.genie.domain.chat.service.ChatService;
import com.example.genie.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;


@Log4j2
@Component
@RequiredArgsConstructor
@Scope("prototype") // 각 팟마다 새로운 인스턴스 생성
public class WebSocketComponent extends TextWebSocketHandler {

    private final UserService userService;
    private final ChatService chatService;

    private Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
    private List<String> chatHistory = new ArrayList<>();
    private String currentDate = "";

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        sessionMap.put(session.getId(), session);
        // 새로운 클라이언트가 입장했음을 다른 클라이언트에게 알리는 메시지를 전송

        // 이전 메시지 전송
        for (String message : chatHistory) {
            sendMessage(session, message);
        }

        Authentication authentication = (Authentication) session.getPrincipal();
        String username = userService.getUserNickName(authentication);
        String joinMessage = username + "님이 입장했습니다.";
        sendMessageToAll(joinMessage);

        // 현재 클라이언트에게 입장 메시지 전송
        sendMessage(session, joinMessage);
    }

    private void sendMessageToAll(String message) {
        sessionMap.values().forEach(session -> {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                log.error("Failed to send message to WebSocket session", e);
            }
        });
    }

    private void sendMessage(WebSocketSession session, String message) throws IOException {
        session.sendMessage(new TextMessage(message));
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws IOException {
        String payload = message.getPayload().toString();
        boolean isDateChanged = checkDateChange(payload);

        if (isDateChanged) {
            String currentDateMessage = createDateMessage();
            sendMessageToAll(currentDateMessage);
        }

        sessionMap.forEach((sessionId, sessionInMap) -> {
            try {
                sessionInMap.sendMessage(message);
            } catch (IOException e) {
                log.error("Failed to send message to WebSocket session", e);
            }
        });

        // 인증 정보 가져오기
        Authentication authentication = (Authentication) session.getAttributes().get("authentication");

        if (authentication != null) {
            chatHistory.add(payload);

            // potId 추출
            Long potId = null;
            try {
                JSONObject jsonObject = new JSONObject(payload);
                potId = jsonObject.getLong("potId");
            } catch (JSONException e) {
                log.error("Failed to extract potId from message payload: {}", payload);
            }

            // potId를 기반으로 채팅 메시지 저장
            if (potId != null) {
                saveChatMessage(authentication, payload, potId);
                log.info("Saved chat message to the database - potId: {}, message: {}", potId, payload);

                // potId를 기반으로 채팅방 메시지 가져오기
                List<String> chatMessages = chatService.getChatMessages(potId);

                // 가져온 메시지를 클라이언트에게 전송
                for (String chatMessage : chatMessages) {
                    sendMessage(session, chatMessage);
                }
            } else {
                log.warn("potId not found in message payload: {}", payload);
            }
        } else {
            log.warn("Authentication not found for WebSocket session");
        }
    }

    private void saveChatMessage(Authentication authentication, String message, Long potId) {
        try {
            chatService.saveChatMessage(authentication, message, potId);
            log.info("Saved chat message to the database");
        } catch (Exception e) {
            log.error("Failed to save chat message to the database", e);
        }
    }

    private boolean checkDateChange(String message) {
        if (chatHistory.isEmpty()) {
            return true;
        }

        String previousMessage = chatHistory.get(chatHistory.size() - 1);
        String currentDateFromMessage = getCurrentDate(message);
        boolean isDateChanged = !currentDateFromMessage.equals(currentDate);

        if (isDateChanged) {
            currentDate = currentDateFromMessage;
        }

        return isDateChanged;
    }

    private String getCurrentDate(String message) {
        return message.substring(0, 10);
    }

    private String createDateMessage() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateMessage = dateFormat.format(new Date());
        return currentDateMessage;
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionMap.remove(session.getId());

        Authentication authentication = (Authentication) session.getAttributes().get("authentication");

        if (authentication != null) {
            String username = userService.getUserNickName(authentication);
            String leaveMessage = username + "님이 퇴장했습니다.";
            sendMessageToAll(leaveMessage);
        } else {
            log.warn("Authentication not found for WebSocket session");
        }
    }
}
