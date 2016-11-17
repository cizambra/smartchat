package com.pxw.smartchat.config.system;

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
        final String pattern = "([^,.!?]+)[.,!?]*";
        final Pattern compiledPattern = Pattern.compile(pattern);
        final Matcher m = compiledPattern.matcher(text);
        final ArrayList<String> result = new ArrayList<>();

        while (m.find()) {
            result.add(m.group(1).trim());
        }

        return result;
    }

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

    private static Boolean isQuestion(final String sentence) {
        final String whWords = "what|which";
        final String primaryHelpingVerbs = "am|is|are|was|were|had|has|have|do|does|did|isn't|aren't|don't|didn't" +
                "|doesn't";
        final String modalHelpingVerbs = "can|could|may|might|will|would|shall|should|must|ought to";
        final String pattern = String.format("(^(%s|%s|%s).*)", whWords, primaryHelpingVerbs, modalHelpingVerbs);
        final Pattern compiledPattern = Pattern.compile(pattern);
        final Matcher m = compiledPattern.matcher(sentence);
        return m.matches();
    }
}
