package com.pxw.smartchat.controller;

import com.pxw.smartchat.config.bot.Bot;
import com.pxw.smartchat.config.system.Route;
import com.pxw.smartchat.model.ChatMessage;
import com.pxw.smartchat.model.Message;
import com.pxw.smartchat.model.statemachine.DefaultStateMachine;
import com.pxw.smartchat.model.statemachine.StateMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class BotController {
    private final String STARTING_STATE = DefaultStateMachine.CONVERSATION_STARTED.name();
    private final String STARTING_DOMAIN = StateMachine.StateMachineType.DEFAULT.name();

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping(Route.HALO)
    @SendTo(Route.INCOMING_BOT_MSG)
    public ChatMessage startConversation() throws Exception {
        final String welcomeMsg = Bot.Response.WELCOME.getMessage();
        waitForBotReply(welcomeMsg);
        return new ChatMessage(welcomeMsg, STARTING_STATE, STARTING_DOMAIN);
    }

    @MessageMapping(Route.INCOMING_USER_MSG)
    @SendTo(Route.INCOMING_BOT_MSG)
    public ChatMessage incomingUserMessage(final ChatMessage payload) throws Exception {
        final ChatMessage botReply = StateMachine.processMessage(payload);
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
