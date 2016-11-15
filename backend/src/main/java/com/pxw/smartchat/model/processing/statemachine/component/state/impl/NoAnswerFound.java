package com.pxw.smartchat.model.processing.statemachine.component.state.impl;

import com.pxw.smartchat.config.exception.librarian.AnswerNotFoundException;
import com.pxw.smartchat.model.messaging.impl.StateMessage;
import com.pxw.smartchat.model.processing.librarian.Librarian;
import com.pxw.smartchat.model.processing.statemachine.component.state.State;
import com.pxw.smartchat.model.processing.statemachine.impl.DefaultStateMachine;
import lombok.RequiredArgsConstructor;

import static com.pxw.smartchat.config.bot.Bot.Response.*;
import static com.pxw.smartchat.model.processing.statemachine.impl.DefaultStateMachine.*;

@RequiredArgsConstructor
public class NoAnswerFound implements State {
    protected final Librarian librarian;

    @Override
    public StateMessage run(String question) throws Exception {
        final String nextState = QUESTION_REVIEWED.name();
        final String nextDomain = DefaultStateMachine.DOMAIN;
        String reply;

        try {
            final String collectedAnswer = librarian.getAnswerFromBase(question);
            final String messageTemplate = ANSWER_FROM_BASE.getMessage();
            reply = String.format(messageTemplate, question, collectedAnswer);
        } catch (final AnswerNotFoundException exception) {
            reply = ANSWER_FROM_BASE_NOT_EXISTS.getMessage();
        }

        return new StateMessage(reply, nextState, nextDomain);
    }
}
