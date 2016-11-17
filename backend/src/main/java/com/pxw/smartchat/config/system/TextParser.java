package com.pxw.smartchat.config.system;

import edu.stanford.nlp.simple.*;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextParser {
    /**
     * This method looks for full stops
     * @param text
     * @return
     */
    public static ArrayList<String> getSentences(final String text) {
        final ArrayList<String> sentences = new ArrayList<>();
        Document doc = new Document(text);
        for (Sentence sent : doc.sentences()) {
            sentences.add(sent.toString());
        }
        return sentences;
    }

    public static ArrayList<String> extractQuestions(final ArrayList<String> sentenceSet) {
        final ArrayList<String> questions = new ArrayList<>();

        for (ListIterator<String> iter = sentenceSet.listIterator(); iter.hasNext();) {
            final String sentence = iter.next();
            System.out.println(sentence);
            if (isQuestion(sentence.toLowerCase(Locale.ENGLISH))) {
                questions.add(sentence);
            }
        }

        return questions;
    }

    private static Boolean isQuestion(final String sentence) {
        final String whWords = "what|which";
        final String primaryHelpingVerbs = "am|is|are|was|were|had|has|have|do|does|did|isn't|aren't|don't|didn't" +
                "|doesn't";
        final String modalHelpingVerbs = "can|could|may|might|will|would|shall|should|must|ought to";
        final String pattern = String.format("(^(.*,\\s*)*(%s|%s|%s).*)", whWords, primaryHelpingVerbs, modalHelpingVerbs);
        final Pattern compiledPattern = Pattern.compile(pattern);
        final Matcher m = compiledPattern.matcher(sentence);
        return m.matches();
    }

    public static ArrayList<String> tokenize(final String text) {
        final ArrayList<String> tokens = new ArrayList<>();

        final Document doc = new Document(text);
        for (Sentence sent : doc.sentences()) {
            tokens.addAll(sent.words());
        }

        return tokens;
    }
}
