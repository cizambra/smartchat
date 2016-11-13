package com.pxw.smartchat.model;

import lombok.NonNull;

import java.util.Locale;

public enum StateMachine {
    CONVERSATION_STARTED() {
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
            return true;
        }
    },

    IS_QUESTION() {
        @Override
        protected ChatMessage runState(ChatMessage message) {
            return new ChatMessage("Question behavior.", name());
        }
    },

    IS_NOT_QUESTION() {
        @Override
        protected ChatMessage runState(ChatMessage message) {
            return null;
        }
    };

    public static ChatMessage processMessage(final @NonNull ChatMessage message) {
        final String stateName = message.getState().toUpperCase(Locale.ENGLISH);
        final ChatMessage stateOutput = StateMachine.valueOf(stateName).runState(message);
        return stateOutput;
    }

    abstract protected ChatMessage runState(final ChatMessage message);
}
