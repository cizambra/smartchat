package com.pxw.smartchat.config.bot;

import com.pxw.smartchat.model.ChatMessage;
import lombok.NonNull;

import java.util.concurrent.ThreadLocalRandom;

public class Bot {
    /**
     * The bot can show different statuses depending on what is doing
     * or saying to the user.
     */
    public enum Status {
        WRITING("Bot is writing a message...");

        private @NonNull
        String message;

        Status(final @NonNull String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    /**
     * This is a list of static messages that the bot is able to say without
     * the usage of a knowledge base.
     */
    public enum Response {
        WELCOME("Hi I'm PO Bot, how can I help you?");

        private @NonNull String message;

        Response(final @NonNull String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    /**
     * This method simulates the bots's action of writing a message back. This means that
     * there should be a delay.
     *
     * @throws Exception
     */
    public static void simulateWriting(final String message) throws Exception {
        final Integer responseTime = message.length() * Behavior.KEYSTOKE_DELAY_MILLI;
        final Integer stdDeviation = 10;
        final Integer lowerBound = responseTime - stdDeviation;
        final Integer upperBound = responseTime + stdDeviation;
        final Integer responseDelay = ThreadLocalRandom.current().nextInt(lowerBound, upperBound + 1);
        Thread.sleep(responseDelay);
    }
}
