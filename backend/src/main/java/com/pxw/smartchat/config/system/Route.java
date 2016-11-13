package com.pxw.smartchat.config.system;

public class Route {
    // Endpoints for internal usage.
    public static final String APPLICATION  = "/app";
    public static final String BROKER       = "/topic";
    public static final String CONNECTION   = "/connect";

    // Endpoints for Client - Server messages (@MessageMapping URIs).
    public static final String HALO                 = "/bot/halo";
    public static final String INCOMING_USER_MSG    = "/bot/new_message";

    // Endpoints for Server - Client messages (@SendTo URIs).
    public static final String INCOMING_BOT_MSG    = "/topic/user/message/received";
    public static final String BOT_WRITING         = "/topic/bot/writing";
}
