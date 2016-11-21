package com.pxw.smartchat.model.knowledge.base.rdf;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.procurement.chatbot.model.Converter;
import com.procurement.chatbot.model.RDFSentence;
import com.procurement.chatbot.parser.Parser;
import com.procurement.rdf.ProcurementRDFRepository;
import com.procurement.rdf.model.RDFQuestion;
import com.pxw.smartchat.model.knowledge.base.exception.AnswerNotFoundException;
import com.pxw.smartchat.model.knowledge.base.exception.MalformedResponseException;
import com.pxw.smartchat.model.knowledge.base.rdf.response.RDFNotFoundResponse;
import com.pxw.smartchat.model.knowledge.base.rdf.response.RDFResponse;

/**
 * This class handles the 3rd party RDF client.
 */
public class RDFConnection {
    private static String ENDPOINT = "http://localhost:8080/openrdf-sesame/";
    private static String REPO_NAME = "procurement" ;
    private final ProcurementRDFRepository client = new ProcurementRDFRepository(ENDPOINT, REPO_NAME);

    public RDFConnection() throws Exception {
    }

    public RDFResponse query(final String payload) throws Exception {
        final Parser parser = new Parser();
        final RDFSentence rdfSentence = parser.parse(payload);
        final RDFQuestion rdfQuestion = Converter.convertToRDFQustion(rdfSentence);
        final String response = client.ask(rdfQuestion);
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(response, RDFResponse.class);
        } catch(final JsonParseException responseParseException) {
            try {
                final RDFNotFoundResponse mappedError = mapper.readValue(response, RDFNotFoundResponse.class);
                throw new AnswerNotFoundException(mappedError.getError().toString());
            } catch (final JsonParseException errorParseException) {
                final String message = String.format("Malformed response from RDF repository: %s", response);
                throw new MalformedResponseException(message);
            }
        }
    }
}
