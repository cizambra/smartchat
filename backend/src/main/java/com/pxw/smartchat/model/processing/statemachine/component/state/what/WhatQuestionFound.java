package com.pxw.smartchat.model.processing.statemachine.component.state.what;

import com.pxw.smartchat.config.system.KeywordExtractor;
import com.pxw.smartchat.model.messaging.impl.StateMessage;
import com.pxw.smartchat.model.processing.statemachine.component.state.State;
import com.pxw.smartchat.model.processing.statemachine.impl.MainStateMachine;

import java.util.ArrayList;

import static com.pxw.smartchat.config.bot.Bot.Response.NOT_A_QUESTION;

public class WhatQuestionFound implements State {
    @Override
    public StateMessage run(String payload) throws Exception {
        final ArrayList<String> keywordSet = KeywordExtractor.getInstance().getKeywords(payload);
        final ArrayList<String> mainConceptSet = KeywordExtractor.getInstance().keywordsWithout(keywordSet, "what");
        final String nextState = MainStateMachine.MESSAGE_RECEIVED.name();
        String reply;

        if (mainConceptSet.isEmpty()) {
            reply = NOT_A_QUESTION.getMessage();
        } else {
            reply = String.format("Your main concept is %s.", mainConceptSet);
        }

        return new StateMessage(reply, nextState, MainStateMachine.DOMAIN);
    }
}
