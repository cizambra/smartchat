package com.pxw.smartchat.config.system;

public class Route {
    // Endpoints for internal usage.
    public static final String APPLICATION  = "/app";
    public static final String BROKER       = "/topic";
    public static final String CONNECTION   = "/connect";

    // Endpoints for Client - Server messages (@MessageMapping URIs).
    public static final String BOT_REPLY    = "/bot/reply";

    // Endpoints for Server - Client messages (@SendTo URIs).
    public static final String USER_MSG_RECEIVED    = "/topic/user/message/received";
    public static final String BOT_REPLYING         = "/topic/bot/writing";
}
