package com.shipogle.app.socket_handlers;


import com.shipogle.app.utility.MyUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class ChatSocketHandler extends TextWebSocketHandler {
    private static ChatSocketHandler instance;

    /**
     * Singleton instance
     *
     * @author Rahul Saliya
     * @return instance of this class
     */
    public static ChatSocketHandler getInstance() {
        if (instance == null)
            instance = new ChatSocketHandler();
        return instance;
    }

    private static final HashMap<String, WebSocketSession> sessions = new HashMap<>();
    public static final String ID_SPLITTER = "!";

    /**
     * Handle text message from client
     *
     * @author Rahul Saliya
     * @param session current session
     * @param message message from client
     * @throws IOException exception
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        System.out.println("Message received: " + payload);

        session.sendMessage(new TextMessage(payload));

        WebSocketSession receiverSession = sessions.get(getSendingUniqueID(session));
        if (receiverSession != null)
            receiverSession.sendMessage(new TextMessage(payload));
    }

    /**
     * Store session in map
     *
     * @author Rahul Saliya
     * @param session connected session
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.put(MyUtils.getUniqueIdForSession(session), session);
    }

    /**
     * Remove session from map
     *
     * @author Rahul Saliya
     * @param session disconnected session
     * @param status status of session
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        WebSocketSession sessionOld = sessions.remove(MyUtils.getUniqueIdForSession(session));
        if (sessionOld != null)
            sessionOld.close();
        super.afterConnectionClosed(session, status);
    }

    /**
     * Get unique id of receiver
     *
     * @author Rahul Saliya
     * @param session current session
     * @return unique id of receiver
     */
    String getSendingUniqueID(WebSocketSession session) {
        String url = Objects.requireNonNull(session.getUri()).toString();
        String userId = url.substring(url.lastIndexOf("/") + 1);
        String[] ids = userId.split(ID_SPLITTER);
        return ids[1] + ID_SPLITTER + ids[0];
    }

    /**
     * Get all sessions
     *
     * @author Rahul Saliya
     * @return all sessions
     */
    public HashMap<String, WebSocketSession> getSessions() {
        return sessions;
    }
}
