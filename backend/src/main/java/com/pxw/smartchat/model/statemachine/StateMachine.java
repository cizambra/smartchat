package com.pxw.smartchat.model.statemachine;

import com.pxw.smartchat.config.exception.ParserException;
import com.pxw.smartchat.model.ChatMessage;
import lombok.NonNull;

import java.util.Locale;

public interface StateMachine {
    enum StateMachineType {
        DEFAULT
    }

    static ChatMessage processMessage(final @NonNull ChatMessage message) throws ParserException {
        final String stateName = message.getState().toUpperCase(Locale.ENGLISH);
        final String stateMachineType = message.getDomain();
        ChatMessage stateOutput;

        if (stateMachineType.equalsIgnoreCase(StateMachineType.DEFAULT.name())) {
            stateOutput = DefaultStateMachine.valueOf(stateName).runState(message);
        } else {
            throw new IllegalArgumentException(String.format("Invalid state machine type: %s", stateMachineType));
        }

        return stateOutput;
    }

    ChatMessage runState(final ChatMessage message) throws ParserException;
}
