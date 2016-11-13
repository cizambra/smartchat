package com.pxw.smartchat.model;

import com.pxw.smartchat.model.processing.Parser;
import com.pxw.smartchat.model.processing.ParserFactory;
import lombok.NonNull;

import java.util.Locale;
import static com.pxw.smartchat.model.processing.ParserFactory.ParserType;

public enum StateMachine {
    CONVERSATION_STARTED {
        // The system checks if user message is a question.
        @Override
        protected ChatMessage runState(ChatMessage message) {
            if (isQuestion(message.getBody())) {
                return new ChatMessage("Cool, you asked something.", IS_QUESTION.name());
            } else {
                return new ChatMessage("Sorry, not a question", IS_NOT_QUESTION.name());
            }
        }

        private Boolean isQuestion(final @NonNull String content) {
            final Parser questionParser = this.contextParser.getParser(ParserType.QUESTION.name());
            return questionParser.matches(content);
        }
    },

    IS_QUESTION {
        @Override
        protected ChatMessage runState(ChatMessage message) {
            return new ChatMessage("Question behavior.", name());
        }
    },

    IS_NOT_QUESTION {
        @Override
        protected ChatMessage runState(ChatMessage message) {
            return null;
        }
    };

    protected ParserFactory contextParser = new ParserFactory();

    public static ChatMessage processMessage(final @NonNull ChatMessage message) {
        final String stateName = message.getState().toUpperCase(Locale.ENGLISH);
        final ChatMessage stateOutput = StateMachine.valueOf(stateName).runState(message);
        return stateOutput;
    }

    abstract protected ChatMessage runState(final ChatMessage message);
}
