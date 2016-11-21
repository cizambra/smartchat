package com.pxw.smartchat.model.knowledge.base.rdf;

import com.pxw.smartchat.config.bot.Bot;
import com.pxw.smartchat.model.knowledge.base.KnowledgeBase;
import com.pxw.smartchat.model.knowledge.base.rdf.exception.RDFAnswerNotFoundException;
import com.pxw.smartchat.model.knowledge.base.rdf.exception.RDFMalformedResponseException;
import com.pxw.smartchat.model.knowledge.base.rdf.response.RDFResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class RDFKnowledgeBase implements KnowledgeBase {
    private final RDFConnection baseConnection;

    /**
     * Gets a raw answer from the RDF service, and compose an
     * answer using that data.
     *
     * @param question
     * @return The answer to the question.
     */
    @Override
    public String getAnswer(final String question) throws Exception {
        try {
            final RDFResponse response = baseConnection.query(question);
            final String[] answers = response.getAnswer();
            return (answers.length > 1) ? String.join("\n", answers) : answers[0];
        } catch(final RDFAnswerNotFoundException answerNotFound) {
            log.info("Exception caught {}", answerNotFound.getMessage(), answerNotFound);
            return Bot.Response.ANSWER_NOT_EXISTS.getMessage();
        } catch(final RDFMalformedResponseException malformedResponse) {
            log.info("Exception caught {}", malformedResponse.getMessage(), malformedResponse);
            return Bot.Response.ERROR_UNKNOWN.getMessage();
        }
    }
}
