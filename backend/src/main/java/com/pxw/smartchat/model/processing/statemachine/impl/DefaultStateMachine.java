package com.pxw.smartchat.model.processing.statemachine.impl;

import com.google.common.base.CaseFormat;
import com.pxw.smartchat.model.processing.statemachine.StateMachine;

public enum DefaultStateMachine implements StateMachine {
    MESSAGE_RECEIVED,
    QUESTION_MATCHED,
    NO_QUESTION_MATCHED,
    CACHED_ANSWER_FOUND,
    SIMILAR_QUESTIONS_FOUND,
    NO_ANSWER_FOUND,
    QUESTION_REVIEWED;

    public String getBeanName() {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
    }
}
