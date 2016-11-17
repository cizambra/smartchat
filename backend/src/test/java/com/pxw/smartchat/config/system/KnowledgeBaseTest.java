package com.pxw.smartchat.config.system;

import com.pxw.smartchat.model.knowledge.Entity;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class KnowledgeBaseTest {
    @Test
    public void testSingleInstance() {
        final KnowledgeBase instance1 = KnowledgeBase.getInstance();
    }

    @Test
    public void testBase() {
        // Nothing.
    }

    @Test
    public void testSearch() {
        final String[] array = {"is", "a", "po"};
        final ArrayList<String> words = new ArrayList<>(Arrays.asList(array));
        final Entity entity = KnowledgeBase.getInstance().searchEntity(words);
    }
}
