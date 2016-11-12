package com.pxw.smartchat.model;

import com.pxw.smartchat.config.system.statemachine.Events;
import com.pxw.smartchat.config.system.statemachine.States;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;

@NoArgsConstructor
@AllArgsConstructor
public class StateMachineRunner {
    @Autowired
    private StateMachine<States, Events> stateMachine;

    public void run() throws Exception {
        stateMachine.sendEvent(Events.E1);
        stateMachine.sendEvent(Events.E2);
    }
}
