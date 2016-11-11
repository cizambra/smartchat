package com.pxw.smartchat.controller;

import com.pxw.smartchat.config.Route;
import com.pxw.smartchat.model.Greeting;
import com.pxw.smartchat.model.HelloMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class RequestController {
    @MessageMapping(Route.HELLO)
    @SendTo(Route.GREETING)
    public Greeting greeting(HelloMessage message) throws Exception {
        return new Greeting("Hello, " + message.getName() + "!");
    }
}
