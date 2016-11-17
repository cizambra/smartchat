package com.pxw.smartchat.config.system;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class TextParserTest {
    private static HashMap<String, String[]> testSet = new HashMap<>();

    static {
        testSet.put("Hello hello. I don't know why you say good bye, I said hello.",
                new String[]{"Hello hello.", "I don't know why you say good bye, I said hello."});
        testSet.put("This is a normal sentence. This is a question, isn't it? This is the end.",
                new String[]{"This is a normal sentence.", "This is a question, isn't it?", "This is the end."});
        testSet.put("Testing a wh- question. What should be my question.",
                new String[]{"Testing a wh- question.", "What should be my question."});
        testSet.put("Testing a helping verb. May this should be my question.",
                new String[]{"Testing a helping verb.", "May this should be my question."});
        testSet.put("Testing a helping verb. Can I have a couple of these?",
                new String[]{"Testing a helping verb.", "Can I have a couple of these?"});
        testSet.put("This is a test of symbols. Are there any problems with \"quotes\", numb3rs and \'quotes\'?",
                new String[]{"This is a test of symbols.",
                        "Are there any problems with \"quotes\", numb3rs and \'quotes\'?"});
    }

    @Test
    public void testSentenceExtraction() {
        Iterator iter = testSet.entrySet().iterator();
        while (iter.hasNext()) {
            final Map.Entry pair = (Map.Entry) iter.next();
            final String[] sentenceArray = (String[]) pair.getValue();
            final ArrayList<String> expectedResult = new ArrayList<>(Arrays.asList(sentenceArray));
            assertEquals(expectedResult, TextParser.getSentences((String) pair.getKey()));
        }
    }

    @Test
    public void testQuestionMatching() {
        Iterator iter = testSet.entrySet().iterator();
        while (iter.hasNext()) {
            final Map.Entry pair = (Map.Entry) iter.next();
            System.out.println(TextParser.extractQuestions(TextParser.getSentences((String) pair.getKey())));
        }
    }

    @Test
    public void testTokenization() {
        final ArrayList<String> tokens = TextParser.tokenize(TextParser.cleanSentence("\"ASIN\" is a word 2345!"));
        System.out.println(tokens);
    }
}
