package com.pxw.smartchat.model.knowledge.base.rdf;

import com.pxw.smartchat.model.knowledge.base.KnowledgeBase;
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
        final RDFResponse response = baseConnection.query(question);
        final String[] answers = response.getAnswer();
        return (answers.length > 1) ? String.join("\n", answers) : answers[0];
    }
}
