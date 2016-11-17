package com.pxw.smartchat.config.system;

import com.pxw.smartchat.model.knowledge.Entity;
import com.pxw.smartchat.model.knowledge.Keyword;
import com.pxw.smartchat.model.knowledge.Relation;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.ListIterator;

public class KnowledgeBase {
    private static final KnowledgeBase instance = new KnowledgeBase();
    private final ArrayList<Entity> base = new ArrayList<>();

    private KnowledgeBase() {
        final ClassLoader classLoader = getClass().getClassLoader();
        final File knowledgeBase = new File(classLoader.getResource("knowledge_base.xml").getFile());
        final String kbPath = knowledgeBase.getAbsolutePath();

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {
                private Entity currentEntity = null;
                private ArrayList<Relation> relationships = null;
                private ArrayList<Keyword> keywords = null;

                public void startElement(final String uri, final String localName, final String qName,
                                         final Attributes attributes) throws SAXException {

                    if (qName.equalsIgnoreCase("entity")) {
                        final String entityName = attributes.getValue("name");
                        final String description = attributes.getValue("description");
                        final Entity entity = new Entity(entityName, description);
                        currentEntity = entity;
                        relationships = new ArrayList<>();
                        keywords = new ArrayList<>();
                    }

                    if(qName.equalsIgnoreCase("relation")) {
                        final String reference = attributes.getValue("ref");
                        final String type = attributes.getValue("type");
                        final Relation relation = new Relation(reference);
                        relationships.add(relation);
                    }

                    if(qName.equalsIgnoreCase("keyword")) {
                        final String value = attributes.getValue("value");
                        final Keyword keyword = new Keyword(value);
                        keywords.add(keyword);
                    }
                }

                public void endElement(final String uri, final String localName, final String qName)
                        throws SAXException {
                    if (qName.equalsIgnoreCase("entity")) {
                        currentEntity.setRelationships(relationships);
                        currentEntity.setKeywords(keywords);
                        base.add(currentEntity);

                        currentEntity = null;
                        relationships = null;
                        keywords = null;
                    }

                }

                public void characters(char ch[], int start, int length) throws SAXException {
                    // Nothing
                }

            };

            saxParser.parse(kbPath, handler);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static KnowledgeBase getInstance() {
        return instance;
    }

    public Entity searchEntity(ArrayList<String> words) {
        Entity currentEntity = null;
        Integer currentMaxCoincidences = 0;
        for (ListIterator<Entity> iter = base.listIterator(); iter.hasNext();) {
            final Entity entity = iter.next();
            Integer coincidences = 0;
            for (ListIterator<String> strIter = words.listIterator(); strIter.hasNext();) {
                final String keywordValue = strIter.next();
                if (entity.getValuesFromKeywords().contains(keywordValue)) {
                    coincidences++;
                }
            }

            if (coincidences > currentMaxCoincidences) {
                currentEntity = entity;
                currentMaxCoincidences = coincidences;
            }
        }

        return currentEntity;
    }
}
