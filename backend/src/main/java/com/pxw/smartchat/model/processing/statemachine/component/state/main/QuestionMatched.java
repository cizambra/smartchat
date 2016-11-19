package com.pxw.smartchat.model.processing.statemachine.component.state.main;

import com.pxw.smartchat.model.knowledge.base.KnowledgeBase;
import com.pxw.smartchat.model.messaging.impl.StateMessage;
import com.pxw.smartchat.model.processing.statemachine.component.state.State;
import com.pxw.smartchat.model.processing.statemachine.impl.MainStateMachine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class QuestionMatched implements State {
    private final KnowledgeBase knowledgeBase;

    @Override
    public StateMessage run(final String question) throws Exception {
        return new StateMessage(knowledgeBase.getAnswer(question),
                MainStateMachine.MESSAGE_RECEIVED.name(), MainStateMachine.DOMAIN);
    }
}
