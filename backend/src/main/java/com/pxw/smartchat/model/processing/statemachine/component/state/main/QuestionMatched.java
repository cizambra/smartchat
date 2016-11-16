package com.pxw.smartchat.model.processing.statemachine.component.state.main;

import com.pxw.smartchat.config.system.KeywordExtractor;
import com.pxw.smartchat.model.messaging.impl.StateMessage;
import com.pxw.smartchat.model.processing.statemachine.StateMachine;
import com.pxw.smartchat.model.processing.statemachine.component.state.State;
import com.pxw.smartchat.model.processing.statemachine.impl.MainStateMachine;
import com.pxw.smartchat.model.processing.statemachine.impl.WhatQuestionStateMachine;
import com.pxw.smartchat.model.processing.statemachine.impl.WhichQuestionStateMachine;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

@RequiredArgsConstructor
public class QuestionMatched implements State {
    @Override
    public StateMessage run(final String question) throws Exception {
        final ArrayList<String> keywordSet = KeywordExtractor.getInstance().getKeywords(question);
        String nextState, nextDomain;

        if (keywordSet.contains("what")) {
            nextState = WhatQuestionStateMachine.WHAT_QUESTION_FOUND.name();
            nextDomain = WhatQuestionStateMachine.DOMAIN;
        } else if (keywordSet.contains("which")) {
            nextState = WhichQuestionStateMachine.WHICH_QUESTION_FOUND.name();
            nextDomain = WhichQuestionStateMachine.DOMAIN;
        } else {
            nextState = MainStateMachine.NO_QUESTION_MATCHED.name();
            nextDomain = MainStateMachine.DOMAIN;
        }

        // Automatic transition to next state.
        return StateMachine.processMessage(new StateMessage(question, nextState, nextDomain));
    }
}
