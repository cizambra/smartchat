package com.pxw.smartchat.model.processing.statemachine.component.state.main;

import com.pxw.smartchat.model.messaging.impl.StateMessage;
import com.pxw.smartchat.model.processing.librarian.Librarian;
import com.pxw.smartchat.model.processing.parser.Parser;
import com.pxw.smartchat.model.processing.statemachine.StateMachine;
import com.pxw.smartchat.model.processing.statemachine.component.state.State;
import com.pxw.smartchat.model.processing.statemachine.impl.MainStateMachine;
import lombok.RequiredArgsConstructor;

import static com.pxw.smartchat.config.bot.Bot.Response.NOT_A_QUESTION;
import static com.pxw.smartchat.model.processing.statemachine.impl.MainStateMachine.*;

/**
 * The user has said something, this state checks if that something is a question or not. If it
 * is a question, it is checked that the question exists on the librarian base.
 */
@RequiredArgsConstructor
public class MessageReceived implements State {
    protected final Librarian librarian;
    protected final Parser questionParser;

    @Override
    public StateMessage run(String payload) throws Exception {
        String reply, nextState;

        if (questionParser.matches(payload)) {
            final String question = questionParser.getPayloadFrom(payload);
            reply = question;
            nextState = QUESTION_MATCHED.name();
        } else {
            reply = NOT_A_QUESTION.getMessage();
            nextState = NO_QUESTION_MATCHED.name();
        }

        return StateMachine.processMessage(new StateMessage(reply, nextState, MainStateMachine.DOMAIN));
    }
}
