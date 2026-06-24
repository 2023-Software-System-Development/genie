package com.example.genie.common.config;

import com.example.genie.common.component.WebSocketComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketComponent webSocketComponent;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(webSocketComponent, "/chat")
                // 모든 origin 허용(.*)은 CSWSH 위험이 있어 명시적 허용 목록으로 제한.
                // 배포 도메인은 여기에 추가할 것.
                .setAllowedOriginPatterns("http://localhost:*", "https://localhost:*")
                .withSockJS()
                .setClientLibraryUrl("https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.5/sockjs.min.js");
    }
}
