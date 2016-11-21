package com.pxw.smartchat.model.knowledge.base.rdf.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RDFMalformedResponseException extends RuntimeException {
    public RDFMalformedResponseException(String message) {
        super(message);
    }

    public RDFMalformedResponseException(Throwable cause) {
        super(cause);
    }

    public RDFMalformedResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}
