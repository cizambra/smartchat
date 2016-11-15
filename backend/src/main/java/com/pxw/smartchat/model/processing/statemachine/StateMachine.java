package com.pxw.smartchat.model.processing.statemachine;

import com.pxw.smartchat.model.messaging.impl.StateMessage;
import com.pxw.smartchat.model.processing.statemachine.component.state.State;
import com.pxw.smartchat.model.processing.statemachine.impl.DefaultStateMachine;
import lombok.NonNull;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Locale;

public interface StateMachine {
    ApplicationContext context = new ClassPathXmlApplicationContext("statemachine.xml");

    static StateMessage processMessage(final @NonNull StateMessage message) throws Exception {
        final String stateName = message.getState().toUpperCase(Locale.ENGLISH);
        final String stateMachineType = message.getDomain();
        StateMessage stateOutput;

        if (stateMachineType == DefaultStateMachine.DOMAIN) {
            stateOutput = DefaultStateMachine.valueOf(stateName).runState(message);
        } else {
            throw new IllegalArgumentException(String.format("Invalid state machine type: %s", stateMachineType));
        }

        return stateOutput;
    }

    default StateMessage runState(final @NonNull StateMessage message) throws Exception {
        final State state = (State) context.getBean(getBeanName());
        return state.run(message.getBody());
    }

    String getBeanName();
}
