package com.pxw.smartchat.config.system;

import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public enum TextParser {
    INSTANCE("stopwords.txt");

    private final ArrayList<String> stopwords = new ArrayList<>();

    TextParser(final String stopwordsBase) {
        final ClassLoader classLoader = getClass().getClassLoader();
        final File stopwordBase = new File(classLoader.getResource(stopwordsBase).getFile());

        try (Scanner scanner = new Scanner(stopwordBase)) {
            while (scanner.hasNextLine()) {
                String stopword = scanner.nextLine();

                // Comments are not considered
                if (stopword.matches("^#.*")) {
                    continue;
                }

                stopwords.add(stopword);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method looks for full stops in the text and then splits the texts
     * using those fullstops as markers.
     *
     * @param text
     * @return A list of sentences
     */
    public static ArrayList<String> getSentences(final String text) {
        final Document doc = new Document(text);
        final ArrayList<String> sentences = new ArrayList<>();
        sentences.addAll(doc.sentences().stream().map(Sentence::toString).collect(Collectors.toList()));
        return sentences;
    }

    /**
     * Search in a list of sentences for those that matches with
     * question patterns.
     *
     * @param sentenceSet
     * @return A list of question-type sentences
     */
    public static ArrayList<String> extractQuestions(final ArrayList<String> sentenceSet) {
        final ArrayList<String> questions = new ArrayList<>();

        for (ListIterator<String> iter = sentenceSet.listIterator(); iter.hasNext();) {
            final String sentence = iter.next();
            if (isQuestion(sentence.toLowerCase(Locale.ENGLISH))) {
                questions.add(sentence);
            }
        }

        return questions;
    }

    /**
     * Checks if a sentence is an interrogative one. For this purpose, an interrogative
     * question contains an introduction, a wh-type question or a helper verb (primary or modal).
     * No question mark is needed for this purpose as it can be misleading.
     *
     * @param sentence
     * @return true if the sentence is a question, false otherwise.
     */
    public static Boolean isQuestion(final String sentence) {
        final String whWords = "what|which";
        final String primaryHelpingVerbs = "am|is|are|was|were|had|has|have|do|does|did|isn't|aren't|don't|didn't" +
                "|doesn't";
        final String modalHelpingVerbs = "can|could|may|might|will|would|shall|should|must|ought to";
        final String pattern = String.format("(^(.*,\\s*)*(%s|%s|%s)\\s.*)", whWords, primaryHelpingVerbs, modalHelpingVerbs);
        final Pattern compiledPattern = Pattern.compile(pattern);
        final Matcher m = compiledPattern.matcher(sentence);
        return m.matches();
    }

    /**
     * Removes common punctuation signs (quotes, dots, commas, question
     * and exclamation marks, colon and semi colon).
     *
     * @param sentence
     * @return Sentence without punctuation mark
     */
    public static String cleanSentence(final String sentence) {
        return sentence.replaceAll("[^a-zA-Z 0-9]+", " ")
                       .replaceAll("\\s[\\s]*", " ")
                       .replaceAll("\"", "")
                       .replaceAll("'([a-z0-9]+)'", "$1")
                       .replaceAll("([a-z0-9]+)[?:!.,;]*", "$1")
                       .trim();
    }

    /**
     * Splits a text into separate words (tokens). Using the space
     * character as a marker (\s).
     *
     * @param text
     * @return List of words.
     */
    public static ArrayList<String> tokenize(final String text) {
        return new ArrayList<>(Arrays.asList(text.split(" ")));
    }

    /**
     * Applies normalizing functions to all the elements in a string
     * collection i.e toLowerCase
     * @param words
     * @return normalized set of strings
     */
    public static ArrayList<String> normalize(final ArrayList<String> words) {
        final ArrayList<String> result = (ArrayList<String>) words.clone();
        log.debug("Started normalization of {}", result);
        result.replaceAll(String::toLowerCase);
        result.replaceAll(String::trim);
        log.debug("Normalization result: {}", result);
        return result;
    }

    /**
     * A full morphological analysis is done to identify the lemma for each word.
     * A lemma is the canonical form, dictionary form, or citation form of a set of words.
     *
     * @param words
     * @return Lemma set
     */
    public static ArrayList<String> lemmatize(final ArrayList<String> words) {
        final String joinedWords = String.join(" ", words);
        final Document doc = new Document(joinedWords);
        final ArrayList<String> lemmas = new ArrayList<>();

        log.debug("Lemmatizing {}", words);
        log.debug("Lemmatization candidate string generated: \"{}\"", joinedWords);

        for (Sentence sent : doc.sentences()) {
            lemmas.addAll(sent.lemmas());
        }

        log.debug("Lemmas generated: {}", lemmas);
        return lemmas;
    }

    /**
     * Counts the instances for each word in the provided list and
     * maps the values using each deduped word as key.
     *
     * @param words
     * @return Map with (word, count) pairs.
     */
    public static Map<String, Integer> countAndDedupe(final ArrayList<String> words) {
        final Map<String, Integer> result = new HashMap<>();
        final Set<String> unique = new HashSet<>(words);

        for (final String key : unique) {
            result.put(key,Collections.frequency(words, key));
        }
        return result;
    }

    /**
     * Checks if a word is a stopword.
     * @param word
     * @return true if the word is a stopword, false otherwise
     */
    public static Boolean isStopword(final String word) {
        return INSTANCE.stopwords.contains(word.toLowerCase(Locale.ENGLISH));
    }

    /**
     * Generates a set of keywords from a text.
     *
     * The set of generated keywords does not contains symbols nor extra
     * whitespaces. All the keywords are lower cased.
     *
     * @param text
     * @return set of keywords
     */
    public static ArrayList<String> getKeywords(final String text) {
        final String sentence = cleanSentence(text);
        final ArrayList<String> wordSet = tokenize(sentence);
        final ArrayList<String> keywordSet = new ArrayList<>();
        final ArrayList<String> removedWords = new ArrayList<>();

        for (ListIterator<String> iter = wordSet.listIterator(); iter.hasNext();) {
            String word = iter.next();
            if (!isStopword(word)) {
                keywordSet.add(word);
            } else {
                // This is done purely for traceability.
                if (!removedWords.contains(word)) {
                    removedWords.add(word);
                }
            }
        }

        log.info("The following stopwords were detected: {}", removedWords);
        return lemmatize(normalize(keywordSet));
    }
}
