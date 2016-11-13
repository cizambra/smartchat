package com.pxw.smartchat.model.processing;

import lombok.NonNull;
import org.springframework.stereotype.Component;

public class QuestionParser implements Parser {
    @Override
    public Boolean matches(final @NonNull String content) {
        return true;
    }
}
