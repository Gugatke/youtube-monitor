package com.gsg.youtubemonitor.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class EventWebSocketHandler extends AbstractWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(EventWebSocketHandler.class);

    private final ConcurrentMap<Integer, WebSocketSession> map = new ConcurrentHashMap<>();

    public void sendUpdateNotification(int userId) {
        WebSocketSession webSocketSession = map.get(userId);
        if (webSocketSession == null) {
            return;
        }

        try {
            webSocketSession.sendMessage(new TextMessage(String.valueOf(userId)));
        } catch (IOException e) {
            log.error("Error occurred sending data update event to user[{}]", userId, e);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Integer idFromQuery = getIdFromQuery(session);
        map.put(idFromQuery, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        Integer idFromQuery = getIdFromQuery(session);
        map.remove(idFromQuery);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private Integer getIdFromQuery(WebSocketSession session) {
        URI uri = session.getUri();
        if (uri == null || uri.getQuery() == null) {
            return null;
        }
        String[] split = uri.getQuery().split("=");
        if (split.length < 2) {
            return null;
        }
        return Integer.parseInt(split[1]);
    }
}
