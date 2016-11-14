package com.pxw.smartchat.model.librarian;

import lombok.NonNull;

import java.util.List;

public class PlainLibrarian implements Librarian {
    @Override
    public Boolean questionExists(final @NonNull String question) {
        return true;
    }

    @Override
    public Boolean similarQuestionsExist(final @NonNull String question) {
        return false;
    }

    @Override
    public List<String> getSimilarQuestions(final @NonNull String question) {
        return null;
    }

    @Override
    public String getAnswerFromCache(final @NonNull String question) {
        return "Default answer from cache.";
    }

    @Override
    public String getAnswerFromBase(final @NonNull String question) {
        return "Default answer from base.";
    }
}
