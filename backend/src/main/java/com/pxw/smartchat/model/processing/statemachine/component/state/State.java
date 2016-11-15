package com.pxw.smartchat.model.processing.statemachine.component.state;

import com.pxw.smartchat.model.messaging.impl.StateMessage;

public interface State {
    StateMessage run(final String payload) throws Exception;
}
