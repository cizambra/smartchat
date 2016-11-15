package com.pxw.smartchat.model.processing.librarian;

import java.util.List;

public interface Librarian {
    Boolean questionExists(final String question);
    Boolean similarQuestionsExist(final String question);
    List<String> getSimilarQuestions(final String question);
    String getAnswerFromCache(final String question);
    String getAnswerFromBase(final String question);
}
