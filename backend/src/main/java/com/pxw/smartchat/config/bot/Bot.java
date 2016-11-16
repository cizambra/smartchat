package com.pxw.smartchat.config.bot;

import lombok.NonNull;

import java.util.concurrent.ThreadLocalRandom;

public class Bot {
    /**
     * The bot can show different statuses depending on what is doing
     * or saying to the user.
     */
    public enum Status {
        WRITING("Bot is writing a message...");

        private @NonNull String message;

        Status(final @NonNull String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    /**
     * This is a list of static messages that the bot is able to say without
     * the usage of a librarian base.
     */
    public enum Response {
        WELCOME("Hi I'm PO Bot, how can I help you?"),
        ANSWER_FROM_CACHE("Your question is: %s\nThe answer provided is %s.\nIf I can help you with something " +
                "else, please provide me a new question."),
        ANSWER_FROM_CACHE_NOT_EXISTS("Something odd has happened, I cannot find the answer to this question! " +
                "Can you email my team and let them know that I was unable to find the answer to this question?\n" +
                "In the meantime, if I can help you with something else, please provide me a new question."),
        ANSWER_FROM_BASE("Your question is: %s\nThe answer provided is %s.\nIf I can help you with something else, " +
                "please provide me a new question."),
        ANSWER_FROM_BASE_NOT_EXISTS("I couldn't find your answer in our knowledge base, if you really need to answer " +
                "this question, please email my team with your question. In the meantime, if I can " +
                "help you with something else, please provide me a new question."),
        NOT_A_QUESTION("I cannot recognize your question :(. If you want, you can try again using " +
                "\"(What|which)\" question format, followed by a question mark (?). If I can help you with " +
                "something else, please provide me a new question.");

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
        final Integer lowerBound = responseTime - 10;
        final Integer upperBound = responseTime + 10;
        final Integer responseDelay = ThreadLocalRandom.current().nextInt(lowerBound, upperBound + 1);
        Thread.sleep(responseDelay);
    }
}
