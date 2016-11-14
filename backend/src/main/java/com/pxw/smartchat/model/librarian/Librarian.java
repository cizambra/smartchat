package com.pxw.smartchat.model.librarian;

import java.util.List;

public interface Librarian {
    Boolean questionExists(final String question);
    Boolean similarQuestionsExist(final String question);
    List<String> getSimilarQuestions(final String question);
    String getAnswerFromCache(final String question);
    String getAnswerFromBase(final String question);
}
