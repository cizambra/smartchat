package com.pxw.smartchat.config.bot;

/**
 * This class allows to customize how the bot behaves when
 * it receives an order.
 */
class Behavior {
    /*
        Taking the base case of keystroke delay for the bot. This delay is measured in
        milliseconds and represents how much will take for the bot "to press" a key.
        This for give a better UX experience (human-like interaction).

        http://stackoverflow.com/questions/22505698/what-is-a-typical-keypress-duration
     */
    public final static Integer KEYSTOKE_DELAY_MILLI = 50;
}
