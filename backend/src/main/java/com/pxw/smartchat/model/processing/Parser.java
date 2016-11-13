package com.pxw.smartchat.model.processing;

import com.pxw.smartchat.config.exception.ParserException;

public interface Parser {
    Boolean matches(final String content);
    String extractPayload(final String content) throws ParserException;
}
