package com.pxw.smartchat.model.knowledge.base.rdf;

/**
 * This class simulates the 3rd party RDF client,
 * that will be provided externally.
 */
public class RDFClientMock {
    public String makeCall(final String question) {
        return "{\"type\" : \"purchase order\"," +
                "\"answer\" : \"Hola\" }";
    }
}
