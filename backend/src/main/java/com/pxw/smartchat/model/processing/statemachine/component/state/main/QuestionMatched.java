package com.pxw.smartchat.model.processing.statemachine.component.state.main;

import com.pxw.smartchat.config.bot.Bot;
import com.pxw.smartchat.model.knowledge.base.KnowledgeBase;
import com.pxw.smartchat.model.knowledge.base.exception.AnswerNotFoundException;
import com.pxw.smartchat.model.knowledge.base.exception.MalformedResponseException;
import com.pxw.smartchat.model.knowledge.base.exception.QuestionNotFoundException;
import com.pxw.smartchat.model.messaging.impl.StateMessage;
import com.pxw.smartchat.model.processing.statemachine.component.state.State;
import com.pxw.smartchat.model.processing.statemachine.impl.MainStateMachine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class QuestionMatched implements State {
    private final KnowledgeBase knowledgeBase;

    @Override
    public StateMessage run(final String question) throws Exception {
        String reply;
        try {
            reply = knowledgeBase.getAnswer(question);
        } catch(final QuestionNotFoundException questionNotFound) {
            reply = Bot.Response.NOT_A_QUESTION.getMessage();
            log.info("Exception caught: \"{}\". Bot replied: \"{}\"", questionNotFound.getMessage(), reply);
        } catch(final AnswerNotFoundException answerNotFound) {
            reply = Bot.Response.ANSWER_NOT_EXISTS.getMessage();
            log.info("Exception caught: \"{}\". Bot replied: \"{}\"", answerNotFound.getMessage(), reply);
        } catch(final MalformedResponseException malformedResponse) {
            reply = Bot.Response.ERROR_UNKNOWN.getMessage();
            log.info("Exception caught: \"{}\". Bot replied: \"{}\"", malformedResponse.getMessage(), reply);
        }
        return new StateMessage(reply, MainStateMachine.MESSAGE_RECEIVED.name(), MainStateMachine.DOMAIN);
    }
}
