package com.pxw.smartchat.model.knowledge.base.rdf;

import com.pxw.smartchat.model.knowledge.base.KnowledgeBase;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.HashMap;

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
        final HashMap<String, String> response = baseConnection.query(question);
        return response.get("answer");
    }
}