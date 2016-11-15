package com.pxw.smartchat.model.processing.statemachine.component.state.impl;

import com.pxw.smartchat.model.messaging.impl.StateMessage;
import com.pxw.smartchat.model.processing.librarian.Librarian;
import com.pxw.smartchat.model.processing.statemachine.StateMachine;
import com.pxw.smartchat.model.processing.statemachine.component.state.State;
import lombok.RequiredArgsConstructor;

import static com.pxw.smartchat.model.processing.statemachine.impl.DefaultStateMachine.*;

@RequiredArgsConstructor
public class QuestionMatched implements State {
    protected final Librarian librarian;

    @Override
    public StateMessage run(final String question) throws Exception {
        String nextState;

        if (librarian.questionExists(question)) {
            // Question exists in the cache and the stored answer is retrieved.
            nextState = CACHED_ANSWER_FOUND.name();
        } else if (librarian.similarQuestionsExist(question)) {
            // Similar questions are shown to the user to choose one.
            nextState = SIMILAR_QUESTIONS_FOUND.name();
        } else {
            // Looks into the repository to find an answer and register this
            // question in the librarian base.
            nextState = NO_ANSWER_FOUND.name();
        }

        // Automatic transition to next state.
        return StateMachine.processMessage(new StateMessage(question, nextState, StateMachineType.DEFAULT.name()));
    }
}
