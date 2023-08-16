package com.shipogle.app.config;

import com.shipogle.app.socket_handlers.ChatSocketHandler;
import com.shipogle.app.socket_handlers.NotificationSocketHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistration;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(MockitoExtension.class)
public class WebSocketConfigTest {

    @Test
    public void testWebSocketOrigins() {
        WebSocketConfigurer configurer = new WebSocketConfig();

        WebSocketHandlerRegistry registry = Mockito.mock(WebSocketHandlerRegistry.class);
        WebSocketHandlerRegistration registration = Mockito.mock(WebSocketHandlerRegistration.class);

        Mockito.when(registry.addHandler(Mockito.any(), Mockito.any())).thenReturn(registration);
        Mockito.when(registration.addHandler(Mockito.any(), Mockito.any())).thenReturn(registration);
        Mockito.when(registration.setAllowedOrigins(Mockito.any())).thenReturn(registration);
        Mockito.when(registration.addInterceptors(Mockito.any())).thenReturn(registration);
        Mockito.when(registration.setHandshakeHandler(Mockito.any())).thenReturn(registration);

        configurer.registerWebSocketHandlers(registry);

        Mockito.verify(registration, Mockito.times(1)).setAllowedOrigins("*");
    }

    @Test
    public void testWebSocketInterceptors() {
        WebSocketConfigurer configurer = new WebSocketConfig();

        WebSocketHandlerRegistry registry = Mockito.mock(WebSocketHandlerRegistry.class);
        WebSocketHandlerRegistration registration = Mockito.mock(WebSocketHandlerRegistration.class);

        Mockito.when(registry.addHandler(Mockito.any(), Mockito.any())).thenReturn(registration);
        Mockito.when(registration.addHandler(Mockito.any(), Mockito.any())).thenReturn(registration);
        Mockito.when(registration.setAllowedOrigins(Mockito.any())).thenReturn(registration);
        Mockito.when(registration.addInterceptors(Mockito.any())).thenReturn(registration);
        Mockito.when(registration.setHandshakeHandler(Mockito.any())).thenReturn(registration);

        configurer.registerWebSocketHandlers(registry);

        Mockito.verify(registration, Mockito.times(1)).addInterceptors(Mockito.any(HttpSessionHandshakeInterceptor.class));
    }

    @Test
    public void testWebSocketHandshakeHandler() {
        WebSocketConfigurer configurer = new WebSocketConfig();

        WebSocketHandlerRegistry registry = Mockito.mock(WebSocketHandlerRegistry.class);
        WebSocketHandlerRegistration registration = Mockito.mock(WebSocketHandlerRegistration.class);

        Mockito.when(registry.addHandler(Mockito.any(), Mockito.any())).thenReturn(registration);
        Mockito.when(registration.addHandler(Mockito.any(), Mockito.any())).thenReturn(registration);
        Mockito.when(registration.setAllowedOrigins(Mockito.any())).thenReturn(registration);
        Mockito.when(registration.addInterceptors(Mockito.any())).thenReturn(registration);
        Mockito.when(registration.setHandshakeHandler(Mockito.any())).thenReturn(registration);

        configurer.registerWebSocketHandlers(registry);

        Mockito.verify(registration, Mockito.times(1)).setHandshakeHandler(Mockito.any(DefaultHandshakeHandler.class));
    }


    @Test
    public void testChatHandlerBean() {
        WebSocketConfig webSocketConfig = new WebSocketConfig();
        ChatSocketHandler chatHandler = webSocketConfig.chatHandler();

        // Assert that the chatHandler bean is not null
        assertNotNull(chatHandler);
    }

    @Test
    public void testNotificationHandlerBean() {
        WebSocketConfig webSocketConfig = new WebSocketConfig();
        NotificationSocketHandler notificationHandler = webSocketConfig.notificationHandler();

        // Assert that the notificationHandler bean is not null
        assertNotNull(notificationHandler);
    }
}

