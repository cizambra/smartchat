package com.pxw.smartchat.config.bot;

public class Bot {
    static class Behavior {
        /*
            Taking the base case of keystroke delay for the bot. This delay is measured in
            milliseconds and represents how much will take for the bot "to press" a key.
            This for give a better UX experience (human-like interaction).

            http://stackoverflow.com/questions/22505698/what-is-a-typical-keypress-duration
         */
        public final static Integer KEYSTOKE_DELAY = 50;
    }

    /**
     * This method simulates the action of writing a message back. This means that
     * there should be a delay.
     *
     * @throws Exception
     */
    public static void simulateWriting(final String message) throws Exception {
        final Integer responseTime = message.length() * Behavior.KEYSTOKE_DELAY;
        Thread.sleep(responseTime);
    }
}
