package com.pxw.smartchat.config.system;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static com.pxw.smartchat.config.system.TextParser.*;

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
            assertEquals(expectedResult, getSentences((String) pair.getKey()));
        }
    }

    @Test
    public void testQuestionMatching() {
        Iterator iter = testSet.entrySet().iterator();
        while (iter.hasNext()) {
            final Map.Entry pair = (Map.Entry) iter.next();
        }
    }

    @Test
    public void testTokenization() {
        final ArrayList<String> expectedOutputList = new ArrayList<>(Arrays.asList(
                new String[]{"\"ASIN\"", "is", "a", "word", "2345!"}));
        final ArrayList<String> outputList = tokenize("\"ASIN\" is a word 2345!");
        assertEquals(expectedOutputList, outputList);
    }

    @Test
    public void testNormalization() {
        final String[] input = {"AFRICA", "LION"};
        final String[] expectedOutput = {"africa", "lion"};
        final ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input));
        final ArrayList<String> expectedOutputList = new ArrayList<>(Arrays.asList(expectedOutput));
        final ArrayList<String> outputList = normalize(inputList);
        assertEquals(expectedOutputList, outputList);
    }

    @Test
    public void testLemmatization() {
        final String[] input = {"are", "operates", "operations"};
        final String[] expectedOutput = {"be", "operate", "operation"};
        final ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input));
        final ArrayList<String> expectedOutputList = new ArrayList<>(Arrays.asList(expectedOutput));
        final ArrayList<String> outputList = lemmatize(inputList);
        assertEquals(expectedOutputList, outputList);
    }
}
