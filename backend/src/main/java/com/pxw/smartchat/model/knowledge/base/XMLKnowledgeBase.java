package com.pxw.smartchat.model.knowledge.base;

import com.pxw.smartchat.model.knowledge.Entity;
import com.pxw.smartchat.model.knowledge.Keyword;
import com.pxw.smartchat.model.knowledge.Relation;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.*;

import static com.pxw.smartchat.config.bot.Bot.Response.ANSWER_NOT_EXISTS;
import static com.pxw.smartchat.config.bot.Bot.Response.NOT_A_QUESTION;
import static com.pxw.smartchat.config.system.TextParser.*;

@Slf4j
public enum XMLKnowledgeBase implements KnowledgeBase {
    INSTANCE("knowledge_base.xml");

    private final ArrayList<Entity> base = new ArrayList<>();
    private class XMLKnowledgeBaseHandler extends DefaultHandler {
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

        public void characters(char ch[], int start, int length) throws SAXException {}
    }

    @Slf4j
    /**
     * Class used as a hack to allow the enum constructor to use
     * logging.
     *
     * http://stackoverflow.com/questions/443980/why-cant-enums-constructor-access-static-fields
     */
    private static class Helper {}

    XMLKnowledgeBase(final @NonNull String sourceFilename) {
        overrideBaseSource(sourceFilename);
    }

    /**
     * Gets the base source file and parse it to fill the base in memory.
     *
     * @param sourceFilename
     */
    public void overrideBaseSource(final @NonNull String sourceFilename) {
        final ClassLoader classLoader = getClass().getClassLoader();
        final File knowledgeBase = new File(classLoader.getResource(sourceFilename).getFile());
        final String kbPath = knowledgeBase.getAbsolutePath();
        Helper.log.debug("Setting base source as {}", sourceFilename);

        try {
            final SAXParserFactory factory = SAXParserFactory.newInstance();
            final SAXParser saxParser = factory.newSAXParser();

            final DefaultHandler handler = new XMLKnowledgeBaseHandler();
            saxParser.parse(kbPath, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a built answer from data in knowledge base.
     *
     * @param question
     * @return
     */
    public String getAnswer(final String question) {
        final ArrayList<String> keywordSet = getKeywords(cleanSentence(question));
        log.info("Identified keywords from question: {}", keywordSet);
        final Entity entity = getHeaviestEntity(keywordSet);
        String reply;

        if (keywordSet.isEmpty()) {
            reply = NOT_A_QUESTION.getMessage();
        } else {
            if (entity != null) {
                final String description = entity.getDescription();
                reply = String.format("%s", description);
            } else {
                reply = ANSWER_NOT_EXISTS.getMessage();
            }
        }
        return reply;
    }

    /**
     * Returns the heaviest entity in the base, given a set of words.
     *
     * @param words
     * @return
     */
    public Entity getHeaviestEntity(final ArrayList<String> words) {
        final HashMap<Entity, Double> weightedEntities = entitiesWeightMap(words);
        Map.Entry<Entity, Double> maxEntry = null;

        for (Map.Entry<Entity, Double> entry : weightedEntities.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }

        return maxEntry != null ? maxEntry.getKey() : null;
    }

    /**
     * Looks up for an entity using its name.
     *
     * @param name
     * @return An entity
     */
    public Entity getEntityByName(final String name) {
        for (final ListIterator<Entity> iter = base.listIterator(); iter.hasNext();) {
            final Entity entity = iter.next();

            if (entity.getName().equals(name)) {
                return entity;
            }
        }

        return null;
    }

    /**
     * Checks for those entities whose keywords contains at least one of the
     * given words in the word set, and assigns a weight to them.
     *
     * @param words The word set given to weight the entities.
     * @return A (entity, weight) map
     */
    private HashMap<Entity, Double> entitiesWeightMap(final ArrayList<String> words) {
        final HashMap<Entity, Double> weightedEntities = new HashMap<>();

        for (final ListIterator<Entity> entityIter = base.listIterator(); entityIter.hasNext();) {
            final Entity entity = entityIter.next();
            Double weight = 0.0;

            for (final ListIterator<String> iter = words.listIterator(); iter.hasNext();) {
                final String word = iter.next();
                final ArrayList<String> keywords = entity.getValuesFromKeywords();

                log.debug("Checking if word \"{}\" is present in lemmatized form of {}", word, keywords);

                // This assignation is made only for traceability purposes.
                final ArrayList<String> lemmatizedKeywords = lemmatize(keywords);
                final Boolean isWordPresent = lemmatizedKeywords.contains(word);
                if (isWordPresent) {
                    // Each coincidence with the keywords increases the weight.
                    weight++;
                }

                final String searchStatus = String.format("Word \"{}\" %sfound in {}",
                        isWordPresent ? "" : "not ");
                log.debug(searchStatus, word, lemmatizedKeywords);
            }

            // If there is at least one coincidence, the entity should be returned
            // into the table.
            if (weight > 0) {
                // The weight starts differentiating more in this point.
                log.debug("Searching matches in description of entity \"{}\" for words: {}",
                        entity.getName(), words);

                weight += getEntityWeight(entity, words);
                weightedEntities.put(entity, weight);
            }
        }

        return weightedEntities;
    }

    /**
     * Returns the weight for an entity in a given condition (a given set of words).
     *
     * @param entity The entity being evaluated
     * @param words The reference words used to calculate the weight for the entity.
     * @return The weight for the entity
     */
    private Double getEntityWeight(final Entity entity, final ArrayList<String> words) {
        final Map<String, Integer> dedupedWords = countAndDedupe(words);
        final Map<String, Double> descFrequencyMap = getTextFrequencyMap(entity.getDescription());
        final Map<String, Double> whitelistedMap = whitelistFrequencyMap(dedupedWords.keySet(), descFrequencyMap);

        for (Map.Entry<String, Double> mapEntry : whitelistedMap.entrySet()) {
            final String word = mapEntry.getKey();
            final Double freq = mapEntry.getValue();

            // If a word in dedupedWords is "heavier", that means that was mentioned more
            // times, and that increases their value in the whitelisted map.
            whitelistedMap.put(word, freq * dedupedWords.get(word));
        }

        final Double weight = whitelistedMap.values().stream().mapToDouble(Number::doubleValue).sum();

        return weight;
    }

    /**
     * Converts a text into a frequency map. That frequency map dedupes the words in the text,
     * and for each word a number corresponding a the percentage of occurrences in the text for that
     * word is mapped.
     *
     * @param text
     * @return
     */
    private Map<String, Double> getTextFrequencyMap(final String text) {
        final ArrayList<String> words = getKeywords(text);
        final Map<String, Double> result = new HashMap<>();
        final Integer wordCount = words.size();
        final Set<String> unique = new HashSet<>(words);

        for (final String key : unique) {
            final Double frequency = (double) Collections.frequency(words, key) / wordCount;
            result.put(key, frequency);
        }
        return result;
    }

    /**
     * Returns the map with only the entries whose keys matches with the whitelisted values.
     *
     * @param whitelist
     * @param map
     * @return
     */
    private Map<String, Double> whitelistFrequencyMap(final Set<String> whitelist,
            final Map<String, Double> map) {
        final Map<String, Double> whitelistedMap = new HashMap<>();

        log.debug("The frequency map is {}", map);

        for (final Map.Entry<String, Double> mapEntry : map.entrySet()) {
            if (whitelist.contains(mapEntry.getKey())) {
                whitelistedMap.put(mapEntry.getKey(), mapEntry.getValue());
            }
        }

        log.info("The whitelisted map is {}", whitelistedMap);
        return whitelistedMap;
    }
}
