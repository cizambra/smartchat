package com.pxw.smartchat.model.processing;

import com.pxw.smartchat.config.exception.ParserException;
import lombok.NonNull;
import org.junit.Test;

import static org.junit.Assert.*;

public class QuestionParserTest {
    private final QuestionParser parser = new QuestionParser();

    @Test
    public void testQuestion() throws ParserException {
        final String message = "Is this a question?";
        assertQuestion(message);
    }

    @Test(expected = ParserException.class)
    public void testNonQuestion() throws ParserException {
        final String message = "This is a string!";
        assertFalse(parser.matches(message));
        // Forcing an extraction when the string is not a question.
        parser.getPayloadFrom(message);
    }

    @Test
    public void testExtendedQuestion() throws ParserException {
        final String message = "Is this a question? Because I think it is.";
        assertQuestion(message);
    }

    @Test
    public void testQuestionAmid() throws ParserException {
        final String message = "This is the introduction. Is this a question? Because I think it is.";
        assertQuestion(message);
    }

    private void assertQuestion(final @NonNull String message) throws ParserException {
        assertTrue(parser.matches(message));
        assertEquals("Is this a question?", parser.getPayloadFrom(message));
    }
}
