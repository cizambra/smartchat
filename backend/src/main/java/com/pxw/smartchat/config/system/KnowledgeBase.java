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
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

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

    public HashMap<Entity, Double> getWeightedEntities(ArrayList<String> words) {
        final HashMap<Entity, Double> weightedEntities = new HashMap<>();

        for (ListIterator<Entity> iter = base.listIterator(); iter.hasNext();) {
            final Entity entity = iter.next();
            // The weight represents the number of coincidences.
            Double weight = 0.0;

            for (ListIterator<String> strIter = words.listIterator(); strIter.hasNext();) {
                final String keywordValue = strIter.next();
                if (entity.getValuesFromKeywords().contains(keywordValue)) {
                    weight++;
                }
            }

            // The weight starts differentiating more in this point.
            weight += getEntityWeight(entity, words);

            // If there is at least one coincidence, the entity should be returned
            // into the table.
            if (weight > 0) {
                weightedEntities.put(entity, weight);
            }
        }

        return weightedEntities;
    }

    private Double getEntityWeight(final Entity entity, final ArrayList<String> words) {
        /* final ArrayList<String> descriptionWords;
        final Integer wordsOnText = words.stream().collect(groupingBy(Function.identity(), counting()));
        Map<String, Integer> frequencyMap = words.stream()
                                                 .collect(toMap(
                                                         s -> s, // key is the word
                                                         s -> 1, // value is 1
                                                         Integer::sum));
        Double weight = 0.0;
        */
        return 0.0;
    }

    public Entity getEntity(ArrayList<String> words) {
        final HashMap<Entity, Double> weightedEntities = getWeightedEntities(words);
        Map.Entry<Entity, Double> maxEntry = null;

        for (Map.Entry<Entity, Double> entry : weightedEntities.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }

        return maxEntry != null ? maxEntry.getKey() : null;
    }
}
