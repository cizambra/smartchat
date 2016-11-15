package com.pxw.smartchat.model.processing.statemachine.component.state.impl;

import com.pxw.smartchat.config.exception.ParserException;
import com.pxw.smartchat.model.messaging.impl.StateMessage;
import com.pxw.smartchat.model.processing.statemachine.component.state.State;

import static com.pxw.smartchat.model.processing.statemachine.impl.DefaultStateMachine.*;

public class NoQuestionMatched implements State {
    @Override
    public StateMessage run(String payload) throws ParserException {
        return new StateMessage(payload, MESSAGE_RECEIVED.name(), StateMachineType.DEFAULT.name());
    }
}
