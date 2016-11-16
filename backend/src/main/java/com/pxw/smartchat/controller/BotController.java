package com.pxw.smartchat.controller;

import com.pxw.smartchat.config.bot.Bot;
import com.pxw.smartchat.config.system.Route;
import com.pxw.smartchat.model.messaging.impl.StateMessage;
import com.pxw.smartchat.model.messaging.Message;
import com.pxw.smartchat.model.processing.statemachine.impl.MainStateMachine;
import com.pxw.smartchat.model.processing.statemachine.StateMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class BotController {
    private final String STARTING_STATE = MainStateMachine.MESSAGE_RECEIVED.name();
    private final String STARTING_DOMAIN = MainStateMachine.DOMAIN;

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping(Route.HALO)
    @SendTo(Route.INCOMING_BOT_MSG)
    public StateMessage startConversation() throws Exception {
        final String welcomeMsg = Bot.Response.WELCOME.getMessage();
        waitForBotReply(welcomeMsg);
        return new StateMessage(welcomeMsg, STARTING_STATE, STARTING_DOMAIN);
    }

    @MessageMapping(Route.INCOMING_USER_MSG)
    @SendTo(Route.INCOMING_BOT_MSG)
    public StateMessage incomingUserMessage(final StateMessage payload) throws Exception {
        final StateMessage botReply = StateMachine.processMessage(payload);
        waitForBotReply(payload.getBody());
        return botReply;
    }

    /**
     * Sends a message with the current status of the bot to the client
     * without the need of a trigger by the former.
     *
     * @throws Exception
     */
    public void sendStatusToClient() throws Exception {
        template.convertAndSend(Route.BOT_WRITING, new Message(Bot.Status.WRITING.getMessage()));
    }

    private void waitForBotReply(final String message) throws Exception {
        // A change of status event is sent to the client.
        sendStatusToClient();
        // After the event is sent the bot waits.
        Bot.simulateWriting(message);
    }
}
