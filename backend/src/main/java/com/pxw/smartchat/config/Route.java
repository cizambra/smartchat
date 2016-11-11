package com.pxw.smartchat.config;

public class Route {
    // Endpoints for internal usage.
    public static final String APPLICATION  = "/app";
    public static final String BROKER       = "/topic";
    public static final String CONNECTION   = "/connect";

    // Endpoints for Server - Client messages.
    public static final String HELLO        = "/hello";

    // Endpoints for Client - Server messages.
    public static final String GREETING     = "/topic/greetings";
}
