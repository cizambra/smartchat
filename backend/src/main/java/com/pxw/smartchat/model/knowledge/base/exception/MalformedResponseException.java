package com.pxw.smartchat.model.knowledge.base.exception;

public class MalformedResponseException extends RuntimeException{
    public MalformedResponseException(String message) {
        super(message);
    }

    public MalformedResponseException(Throwable cause) {
        super(cause);
    }

    public MalformedResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}
