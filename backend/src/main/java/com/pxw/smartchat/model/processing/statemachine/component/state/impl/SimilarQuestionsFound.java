package com.pxw.smartchat.model.processing.statemachine.component.state.impl;

import com.pxw.smartchat.model.messaging.impl.StateMessage;
import com.pxw.smartchat.model.processing.librarian.Librarian;
import com.pxw.smartchat.model.processing.statemachine.component.state.State;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.pxw.smartchat.model.processing.statemachine.impl.DefaultStateMachine.*;

@RequiredArgsConstructor
public class SimilarQuestionsFound implements State {
    protected final Librarian librarian;

    @Override
    public StateMessage run(String payload) throws Exception {
        final String question = payload;
        final List<String> questionSet = librarian.getSimilarQuestions(question);
        final String reply = String.format("The question was not found, are you trying to answer one of this " +
                "questions?\n%s", question);
        final String nextState = QUESTION_REVIEWED.name();
        final String nextDomain = StateMachineType.DEFAULT.name();

        return new StateMessage(reply, nextState, nextDomain);
    }
}
