package com.pxw.smartchat.model.knowledge.base.rdf;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;

/**
 * This class will handle the 3rd party RDF client.
 */
public class RDFConnection {
    private final RDFClientMock client = new RDFClientMock();

    public HashMap<String, String> query(final String payload) throws IOException {
        final String response = client.makeCall(payload);
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response,
                                new TypeReference<HashMap<String, Object>>() {});

    }
}
