package com.pxw.smartchat.model.statemachine;

import com.pxw.smartchat.config.bot.Bot;
import com.pxw.smartchat.config.exception.ParserException;
import com.pxw.smartchat.config.exception.librarian.AnswerNotFoundException;
import com.pxw.smartchat.model.ChatMessage;
import com.pxw.smartchat.model.librarian.Librarian;
import com.pxw.smartchat.model.librarian.LibrarianFactory;
import com.pxw.smartchat.model.processing.Parser;
import com.pxw.smartchat.model.processing.ParserFactory;
import lombok.NonNull;

import java.util.List;
import static com.pxw.smartchat.model.processing.ParserFactory.ParserType;
import static com.pxw.smartchat.model.librarian.LibrarianFactory.LibrarianType;

public enum DefaultStateMachine implements StateMachine {
    /**
     * The user has said something, this state checks if that something is a question or not. If it
     * is a question, it is checked that the question exists on the librarian base.
     */
    CONVERSATION_STARTED {
        @Override
        public ChatMessage runState(final @NonNull ChatMessage message) throws ParserException {
            final Parser questionParser = contextParser.getParser(ParserType.QUESTION.name());
            String reply, nextState, nextDomain = StateMachineType.DEFAULT.name();

            if (questionParser.matches(message.getBody())) {
                final String question = questionParser.getPayloadFrom(message.getBody());

                if (librarian.questionExists(question)) {
                    // Question exists in the cache and the stored answer is retrieved.
                    try {
                        final String collectedAnswer = librarian.getAnswerFromCache(question);
                        final String messageTemplate = Bot.Response.ANSWER_FROM_CACHE.getMessage();

                        reply = String.format(messageTemplate, question, collectedAnswer);
                    } catch (final AnswerNotFoundException exception) {
                        reply = Bot.Response.ANSWER_FROM_CACHE_NOT_EXISTS.getMessage();
                    }
                    nextState = CONVERSATION_STARTED.name();
                } else if (librarian.similarQuestionsExist(question)) {
                    // Similar questions are shown to the user to choose one.
                    final List<String> questionSet = librarian.getSimilarQuestions(question);

                    reply = String.format("The question was not found, are you trying to answer one of this " +
                            "questions?\n%s", question);
                    nextState = USER_CORRECTED_QUESTION.name();
                } else {
                    // Looks into the repository to find an answer and register this
                    // question in the librarian base.
                    try {
                        final String collectedAnswer = librarian.getAnswerFromBase(question);
                        final String messageTemplate = Bot.Response.ANSWER_FROM_BASE.getMessage();

                        reply = String.format(messageTemplate, question, collectedAnswer);
                    } catch (final AnswerNotFoundException exception) {
                        reply = Bot.Response.ANSWER_FROM_BASE_NOT_EXISTS.getMessage();
                    }
                    nextState = CONVERSATION_STARTED.name();
                }
            } else {
                reply = Bot.Response.NOT_A_QUESTION.getMessage();
                nextState = CONVERSATION_STARTED.name();
            }

            return new ChatMessage(reply, nextState, nextDomain);
        }
    },

    USER_CORRECTED_QUESTION {
        @Override
        public ChatMessage runState(final @NonNull ChatMessage message) {
            return new ChatMessage("Question behavior.", name(), StateMachineType.DEFAULT.name());
        }
    };

    protected final ParserFactory contextParser = new ParserFactory();
    protected final Librarian librarian = new LibrarianFactory().getLibrarian(LibrarianType.PLAIN.name());
}
