package com.pxw.smartchat.model.processing.statemachine.impl;

import com.google.common.base.CaseFormat;
import com.pxw.smartchat.model.processing.statemachine.StateMachine;

public enum WhichQuestionStateMachine implements StateMachine {
    WHICH_QUESTION_FOUND;

    public static String DOMAIN = WhichQuestionStateMachine.class.getSimpleName();
    public String getBeanName() {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
    }
}
