package com.pxw.smartchat.model.processing;

import lombok.NonNull;

public class ParserFactory {
    public enum ParserType {
        QUESTION
    }

    public Parser getParser(final @NonNull String parserType) {
        if (parserType.equalsIgnoreCase(ParserType.QUESTION.name())) {
            return new QuestionParser();
        } else {
            throw new IllegalArgumentException(String.format("Invalid parser type: %s", parserType));
        }
    }
}
