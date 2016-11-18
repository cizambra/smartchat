package com.pxw.smartchat.model.knowledge.base;

import com.pxw.smartchat.model.knowledge.Entity;

import java.util.ArrayList;

public interface KnowledgeBase {
    Entity getHeaviestEntity(final ArrayList<String> words);
    Entity getEntityByName(final String name);
}
