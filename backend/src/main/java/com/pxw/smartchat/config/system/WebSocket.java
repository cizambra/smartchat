package com.pxw.smartchat.config.system;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocket extends AbstractWebSocketMessageBrokerConfigurer {
    /*
        This value is set to allow files not-in-server routes (as file://xxxxx)
        can make calls to the server. This variable should be removed once the UI
        is set as a symlink in the web server.
     */
    private final String ALLOWED_ORIGINS_PATTERN = "*";

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(Route.BROKER);
        config.setApplicationDestinationPrefixes(Route.APPLICATION);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(Route.CONNECTION)
                .setAllowedOrigins(ALLOWED_ORIGINS_PATTERN)
                .withSockJS();
    }

}