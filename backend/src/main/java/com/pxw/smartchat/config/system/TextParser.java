package com.pxw.smartchat.config.system;

import edu.stanford.nlp.simple.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TextParser {
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
                questions.add(cleanSentence(sentence));
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
        return sentence.replaceAll("\"", "")
                       .replaceAll("'([a-z0-9]+)'", "$1")
                       .replaceAll("([a-z0-9]+)[?:!.,;]*", "$1");
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
}
