package com.pxw.smartchat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
    /*
        TODO:
        setAllowedOrigins is <*> right now just because otherwise it will become impossible to test from a
        local HTML file. Here we have two choices:
            - Move the view into the maven scope.
            - Modify the pattern to point the GUI origin once it is running in some service.
    */
    private static String ALLOWED_ORIGINS_PATTERN = "*";

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/gs-guide-websocket").setAllowedOrigins(ALLOWED_ORIGINS_PATTERN).withSockJS();
    }

}