package com.pxw.smartchat.config.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ParserException extends Exception {

    public ParserException(String message) {
        super(message);
    }

    public ParserException(Throwable cause) {
        super(cause);
    }

    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
