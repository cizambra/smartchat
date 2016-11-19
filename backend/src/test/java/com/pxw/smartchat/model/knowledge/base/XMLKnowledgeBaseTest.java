package com.pxw.smartchat.model.knowledge.base;

import com.pxw.smartchat.model.knowledge.Entity;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class XMLKnowledgeBaseTest {
    @Test
    public void testSearch() {
        XMLKnowledgeBase.INSTANCE.overrideBaseSource("knowledge_base_test.xml");

        final String[] array = {"africa", "lion"};
        final ArrayList<String> words = new ArrayList<>(Arrays.asList(array));
        final XMLKnowledgeBase base = XMLKnowledgeBase.INSTANCE;
        final Entity entity = base.getHeaviestEntity(words);
        assertEquals(base.getEntityByName("lion"), entity);
    }
}
