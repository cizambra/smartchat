package com.pxw.smartchat.model.processing.parser.impl;

import com.pxw.smartchat.config.exception.ParserException;
import com.pxw.smartchat.model.processing.parser.Parser;
import lombok.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuestionParser implements Parser {
    private final String pattern = "(.*[.,])?(.*\\?)(.*)?";
    private final Pattern compiledPattern = Pattern.compile(pattern);

    @Override
    public Boolean matches(final @NonNull String content) {
        final Matcher m = compiledPattern.matcher(content);
        return m.matches();
    }

    @Override
    public String getPayloadFrom(String content) throws ParserException {
        final Matcher m = compiledPattern.matcher(content);

        if (!m.find()) {
            throw new ParserException(String.format("Content is not a question %s", content));
        }

        return m.group(2).trim();
    }
}
