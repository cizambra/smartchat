package com.pxw.smartchat.controller;

import com.pxw.smartchat.config.bot.Reply;
import com.pxw.smartchat.config.system.Route;
import com.pxw.smartchat.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class BotController {
    private SimpMessagingTemplate template;

    @Autowired
    public BotController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping(Route.BOT_REPLY)
    @SendTo(Route.USER_MSG_RECEIVED)
    public Message sendReply(Message userMessage) throws Exception {
        sendStatus();
        Thread.sleep(1000); // We want to simulate delay for UX improvement.
        return new Message(userMessage.getBody());
    }

    /**
     * Sends a message with the current status of the bot to the client
     * without the need of a trigger by the former.
     *
     * @throws Exception
     */
    public void sendStatus() throws Exception {
        template.convertAndSend(Route.BOT_REPLYING, new Message(Reply.BOT_WRITING));
    }
}
