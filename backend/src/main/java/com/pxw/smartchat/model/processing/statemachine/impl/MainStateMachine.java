package com.pxw.smartchat.model.processing.statemachine.impl;

import com.google.common.base.CaseFormat;
import com.pxw.smartchat.model.processing.statemachine.StateMachine;

public enum MainStateMachine implements StateMachine {
    MESSAGE_RECEIVED,
    QUESTION_MATCHED,
    NO_QUESTION_MATCHED;

    public static String DOMAIN = MainStateMachine.class.getSimpleName();
    public String getBeanName() {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
    }
}
