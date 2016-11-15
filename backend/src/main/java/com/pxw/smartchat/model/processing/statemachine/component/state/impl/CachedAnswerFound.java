package com.pxw.smartchat.model.processing.statemachine.component.state.impl;

import com.pxw.smartchat.config.exception.librarian.AnswerNotFoundException;
import com.pxw.smartchat.model.messaging.impl.StateMessage;
import com.pxw.smartchat.model.processing.librarian.Librarian;
import com.pxw.smartchat.model.processing.statemachine.component.state.State;
import lombok.RequiredArgsConstructor;

import static com.pxw.smartchat.config.bot.Bot.Response.*;
import static com.pxw.smartchat.model.processing.statemachine.impl.DefaultStateMachine.*;

/**
 * This event registers when an answer to a question has been found in
 * the cache (this cache can be a DB table, plain text file, etc.) and
 * returns the message to be sent to the user.
 */
@RequiredArgsConstructor
public class CachedAnswerFound implements State {
    protected final Librarian librarian;

    @Override
    public StateMessage run(String question) throws Exception {
        final String nextState = MESSAGE_RECEIVED.name();
        final String nextDomain = StateMachineType.DEFAULT.name();
        String reply;

        try {
            final String collectedAnswer = librarian.getAnswerFromCache(question);
            final String messageTemplate = ANSWER_FROM_CACHE.getMessage();
            reply = String.format(messageTemplate, question, collectedAnswer);
        } catch (final AnswerNotFoundException exception) {
            reply = ANSWER_FROM_CACHE_NOT_EXISTS.getMessage();
        }

        return new StateMessage(reply, nextState, nextDomain);
    }
}
