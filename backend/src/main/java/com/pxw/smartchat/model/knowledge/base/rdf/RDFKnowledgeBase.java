package com.pxw.smartchat.model.knowledge.base.rdf;

import com.pxw.smartchat.model.knowledge.base.KnowledgeBase;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RDFKnowledgeBase implements KnowledgeBase {
    private final RDFConnection baseConnection;

    @Override
    public String getAnswer(final String question) {
        baseConnection.makeRequest(question);
        return null;
    }
}
