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
                reply = question;
                nextState = MESSAGE_IS_QUESTION.name();
            } else {
                reply = Bot.Response.NOT_A_QUESTION.getMessage();
                nextState = MESSAGE_IS_NOT_QUESTION.name();
            }

            return StateMachine.processMessage(new ChatMessage(reply, nextState, nextDomain));
        }
    },

    MESSAGE_IS_QUESTION {
        @Override
        public ChatMessage runState(final @NonNull ChatMessage message) throws ParserException {
            final String question = message.getBody();
            String nextState, nextDomain = StateMachineType.DEFAULT.name();

            if (librarian.questionExists(question)) {
                // Question exists in the cache and the stored answer is retrieved.
                nextState = ANSWER_EXISTS_IN_CACHE.name();
            } else if (librarian.similarQuestionsExist(question)) {
                // Similar questions are shown to the user to choose one.
                nextState = SIMILAR_QUESTIONS_EXIST.name();
            } else {
                // Looks into the repository to find an answer and register this
                // question in the librarian base.
                nextState = NEW_ANSWER_REQUIRED.name();
            }

            return StateMachine.processMessage(new ChatMessage(question, nextState, nextDomain));
        }
    },

    MESSAGE_IS_NOT_QUESTION {
        @Override
        public ChatMessage runState(final @NonNull ChatMessage message) throws ParserException {
            final String nextState = CONVERSATION_STARTED.name();
            final String nextDomain = StateMachineType.DEFAULT.name();
            return new ChatMessage(message.getBody(), nextState, nextDomain);
        }
    },

    ANSWER_EXISTS_IN_CACHE {
        @Override
        public ChatMessage runState(final @NonNull ChatMessage message) throws ParserException {
            final String question = message.getBody();
            final String nextState = CONVERSATION_STARTED.name();
            final String nextDomain = StateMachineType.DEFAULT.name();
            String reply;

            try {
                final String collectedAnswer = librarian.getAnswerFromCache(question);
                final String messageTemplate = Bot.Response.ANSWER_FROM_CACHE.getMessage();
                reply = String.format(messageTemplate, question, collectedAnswer);
            } catch (final AnswerNotFoundException exception) {
                reply = Bot.Response.ANSWER_FROM_CACHE_NOT_EXISTS.getMessage();
            }

            return new ChatMessage(reply, nextState, nextDomain);
        }
    },

    SIMILAR_QUESTIONS_EXIST {
        @Override
        public ChatMessage runState(final @NonNull ChatMessage message) throws ParserException {
            final String question = message.getBody();
            final List<String> questionSet = librarian.getSimilarQuestions(question);
            final String reply = String.format("The question was not found, are you trying to answer one of this " +
                    "questions?\n%s", question);
            final String nextState = USER_CORRECTED_QUESTION.name();
            final String nextDomain = StateMachineType.DEFAULT.name();

            return new ChatMessage(reply, nextState, nextDomain);
        }
    },

    NEW_ANSWER_REQUIRED {
        @Override
        public ChatMessage runState(final @NonNull ChatMessage message) throws ParserException {
            final String question = message.getBody();
            final String nextState = USER_CORRECTED_QUESTION.name();
            final String nextDomain = StateMachineType.DEFAULT.name();
            String reply;

            try {
                final String collectedAnswer = librarian.getAnswerFromBase(question);
                final String messageTemplate = Bot.Response.ANSWER_FROM_BASE.getMessage();
                reply = String.format(messageTemplate, question, collectedAnswer);
            } catch (final AnswerNotFoundException exception) {
                reply = Bot.Response.ANSWER_FROM_BASE_NOT_EXISTS.getMessage();
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
