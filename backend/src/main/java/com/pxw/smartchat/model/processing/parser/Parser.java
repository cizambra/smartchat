package com.pxw.smartchat.model.processing.parser;

import com.pxw.smartchat.config.exception.ParserException;

public interface Parser {
    Boolean matches(final String content);
    String getPayloadFrom(final String content) throws ParserException;
}
