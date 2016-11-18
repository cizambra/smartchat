package com.pxw.smartchat.model.processing.statemachine.component.state.main;

import com.pxw.smartchat.model.knowledge.Entity;
import com.pxw.smartchat.model.knowledge.base.KnowledgeBase;
import com.pxw.smartchat.model.messaging.impl.StateMessage;
import com.pxw.smartchat.model.processing.statemachine.component.state.State;
import com.pxw.smartchat.model.processing.statemachine.impl.MainStateMachine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

import static com.pxw.smartchat.config.bot.Bot.Response.ANSWER_NOT_EXISTS;
import static com.pxw.smartchat.config.bot.Bot.Response.NOT_A_QUESTION;
import static com.pxw.smartchat.config.system.TextParser.*;

@RequiredArgsConstructor
@Slf4j
public class QuestionMatched implements State {
    private final KnowledgeBase knowledgeBase;

    @Override
    public StateMessage run(final String question) throws Exception {
        final ArrayList<String> keywordSet = getKeywords(question);
        final String nextState = MainStateMachine.MESSAGE_RECEIVED.name();
        String reply;

        log.info("Identified keywords from question: {}", keywordSet);

        if (keywordSet.isEmpty()) {
            reply = NOT_A_QUESTION.getMessage();
        } else {
            final Entity entity = knowledgeBase.getHeaviestEntity(keywordSet);
            if (entity != null) {
                final String description = entity.getDescription();
                reply = String.format("%s", description);
            } else {
                reply = ANSWER_NOT_EXISTS.getMessage();
            }
        }

        return new StateMessage(reply, nextState, MainStateMachine.DOMAIN);
    }
}
