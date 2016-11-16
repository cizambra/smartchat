package com.pxw.smartchat.model.processing.statemachine.impl;

import com.google.common.base.CaseFormat;
import com.pxw.smartchat.model.processing.statemachine.StateMachine;

public enum WhatQuestionStateMachine implements StateMachine {
    WHAT_QUESTION_FOUND;

    public static String DOMAIN = WhatQuestionStateMachine.class.getSimpleName();
    public String getBeanName() {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
    }
}
