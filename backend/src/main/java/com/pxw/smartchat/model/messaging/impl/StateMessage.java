package com.pxw.smartchat.model.messaging.impl;

import com.pxw.smartchat.model.messaging.Message;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * This type of message is a regular one, but also containing a
 * state. The state represents the stage in where the conversation is
 * in the represented state machine.
 *
 * If the message is an incoming one, then the state represents the
 * previous state, i.e. the state the bot had at the moment of sending
 * the message. If the message is an outgoing one, then the state
 * represents the state the bot have at the moment of sending the message.
 */
@NoArgsConstructor
public class StateMessage extends Message {
    private @NonNull String state;
    private @NonNull String domain;

    public StateMessage(@NonNull String payload, @NonNull String state, @NonNull String domain) {
        super(payload);
        this.state = state;
        this.domain = domain;
    }

    public String getState() {
        return state;
    }

    public String getDomain() { return domain; }
}
