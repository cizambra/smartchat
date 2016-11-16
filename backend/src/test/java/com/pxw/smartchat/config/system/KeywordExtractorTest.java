package com.pxw.smartchat.config.system;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class KeywordExtractorTest {
    @Test
    public void testSingleInstance() {
        final KeywordExtractor instance1 = KeywordExtractor.getInstance();
        final KeywordExtractor instance2 = KeywordExtractor.getInstance();
        assertEquals(instance1, instance2);
    }

    @Test
    public void testIsStepword() {
        assertTrue(KeywordExtractor.getInstance().isStepword("Hi"));
    }

    @Test
    public void testCleanPhrase() {
        final String examplePhrase = "  What is the ASIN associated with this purchase order? ";
        final String resultingPhrase = "What is the ASIN associated with this purchase order";
        assertEquals(resultingPhrase, KeywordExtractor.getInstance().cleanPhrase(examplePhrase));
    }

    @Test
    public void testGetKeywords() {
        final String examplePhrase = "What is the ASIN associated with this purchase order";
        final String[] keywordArray = {"what", "asin", "associated", "purchase", "order"};
        final ArrayList<String> keywordSet = new ArrayList<>(Arrays.asList(keywordArray));

        assertEquals(keywordSet, KeywordExtractor.getInstance().getKeywords(examplePhrase));
    }
}
