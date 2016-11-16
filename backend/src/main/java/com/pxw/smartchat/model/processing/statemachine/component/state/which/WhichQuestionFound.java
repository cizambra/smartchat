package com.pxw.smartchat.model.processing.statemachine.component.state.which;

import com.pxw.smartchat.model.messaging.impl.StateMessage;
import com.pxw.smartchat.model.processing.statemachine.component.state.State;
import com.pxw.smartchat.model.processing.statemachine.impl.MainStateMachine;

public class WhichQuestionFound implements State {
    @Override
    public StateMessage run(String payload) throws Exception {
        final String nextState = MainStateMachine.MESSAGE_RECEIVED.name();
        return new StateMessage("This is a Which question.", nextState, MainStateMachine.DOMAIN);
    }
}
