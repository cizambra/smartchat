package com.pxw.smartchat.model.knowledge.base.rdf;

import java.util.HashMap;

/**
 * This class will handle the 3rd party RDF client.
 */
public class RDFConnection {
    private final Object client;

    RDFConnection() {
        client = new Object();
    }

    public HashMap<String, String> makeRequest(final String payload) {
        HashMap<String, String> hm = new HashMap<>();
        // Operation
        client.doSomething(payload);
        return hm;
    }
}
