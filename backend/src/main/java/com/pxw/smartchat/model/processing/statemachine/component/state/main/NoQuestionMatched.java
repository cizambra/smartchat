package com.pxw.smartchat.model.processing.statemachine.component.state.main;

import com.pxw.smartchat.model.messaging.impl.StateMessage;
import com.pxw.smartchat.model.processing.statemachine.component.state.State;
import com.pxw.smartchat.model.processing.statemachine.impl.MainStateMachine;

import static com.pxw.smartchat.model.processing.statemachine.impl.MainStateMachine.MESSAGE_RECEIVED;

public class NoQuestionMatched implements State {
    @Override
    public StateMessage run(String payload) throws Exception {
        return new StateMessage(payload, MESSAGE_RECEIVED.name(), MainStateMachine.DOMAIN);
    }
}
