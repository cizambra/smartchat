package com.pxw.smartchat.model.processing.statemachine.component.state.main;

import com.pxw.smartchat.model.messaging.impl.StateMessage;
import com.pxw.smartchat.model.processing.statemachine.StateMachine;
import com.pxw.smartchat.model.processing.statemachine.component.state.State;
import com.pxw.smartchat.model.processing.statemachine.impl.MainStateMachine;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

import static com.pxw.smartchat.config.bot.Bot.Response.NOT_A_QUESTION;
import static com.pxw.smartchat.model.processing.statemachine.impl.MainStateMachine.NO_QUESTION_MATCHED;
import static com.pxw.smartchat.model.processing.statemachine.impl.MainStateMachine.QUESTION_MATCHED;
import static com.pxw.smartchat.config.system.TextParser.*;

/**
 * The user has said something, this state checks if that something is a question or not. If it
 * is a question, it is checked that the question exists on the librarian base.
 */
@RequiredArgsConstructor
public class MessageReceived implements State {
    @Override
    public StateMessage run(final String message) throws Exception {
        final ArrayList<String> sentences = getSentences(message);
        final ArrayList<String> questions = extractQuestions(sentences);
        String reply, nextState;

        if (!questions.isEmpty()) {
            final String question = questions.get(0);
            reply = question;
            nextState = QUESTION_MATCHED.name();
        } else {
            reply = NOT_A_QUESTION.getMessage();
            nextState = NO_QUESTION_MATCHED.name();
        }

        return StateMachine.processMessage(new StateMessage(reply, nextState, MainStateMachine.DOMAIN));
    }
}
