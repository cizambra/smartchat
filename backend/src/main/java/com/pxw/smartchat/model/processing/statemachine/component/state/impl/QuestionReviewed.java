package com.pxw.smartchat.model.processing.statemachine.component.state.impl;

import com.pxw.smartchat.model.messaging.impl.StateMessage;
import com.pxw.smartchat.model.processing.statemachine.component.state.State;
import com.pxw.smartchat.model.processing.statemachine.impl.DefaultStateMachine;

import static com.pxw.smartchat.model.processing.statemachine.impl.DefaultStateMachine.*;

public class QuestionReviewed implements State {
    @Override
    public StateMessage run(String payload) throws Exception {
        return new StateMessage("Question behavior.", MESSAGE_RECEIVED.name(), DefaultStateMachine.DOMAIN);
    }
}
