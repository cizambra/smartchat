package com.pxw.smartchat.model.knowledge.base.rdf.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RDFAnswerNotFoundException extends RuntimeException {
    public RDFAnswerNotFoundException(String message) {
        super(message);
    }

    public RDFAnswerNotFoundException(Throwable cause) {
        super(cause);
    }

    public RDFAnswerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

