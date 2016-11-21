package com.pxw.smartchat.model.knowledge.base.rdf;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pxw.smartchat.model.knowledge.base.rdf.exception.RDFAnswerNotFoundException;
import com.pxw.smartchat.model.knowledge.base.rdf.exception.RDFMalformedResponseException;
import com.pxw.smartchat.model.knowledge.base.rdf.response.RDFNotFoundResponse;
import com.pxw.smartchat.model.knowledge.base.rdf.response.RDFResponse;

/**
 * This class handles the 3rd party RDF client.
 */
public class RDFConnection {
    private final RDFClientMock client = new RDFClientMock();

    public RDFResponse query(final String payload) throws Exception {
        final String response = client.makeCall(payload);
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(response, RDFResponse.class);
        } catch(final JsonParseException responseParseException) {
            try {
                final RDFNotFoundResponse mappedError = mapper.readValue(response, RDFNotFoundResponse.class);
                throw new RDFAnswerNotFoundException(mappedError.getError().toString());
            } catch (final JsonParseException errorParseException) {
                final String message = String.format("Malformed response from RDF repository: %s", response);
                throw new RDFMalformedResponseException(message);
            }
        }
    }
}
