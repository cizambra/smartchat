package com.pxw.smartchat.config.system;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class KeywordExtractor {
    private static final KeywordExtractor instance = new KeywordExtractor();
    private final ArrayList<String> blacklist = new ArrayList<>();

    private KeywordExtractor() {
        final ClassLoader classLoader = getClass().getClassLoader();
        final File stopwordBase = new File(classLoader.getResource("stopwords.txt").getFile());

        try (Scanner scanner = new Scanner(stopwordBase)) {

            while (scanner.hasNextLine()) {
                String stopword = scanner.nextLine();

                // Comments are not considered
                if (stopword.matches("^#.*")) {
                    continue;
                }

                blacklist.add(stopword);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static KeywordExtractor getInstance() {
        return instance;
    }

    public Boolean isStepword(final String word) {
        return blacklist.contains(word.toLowerCase(Locale.ENGLISH));
    }

    public String cleanPhrase(final String phrase) {
        String cleanPhrase = phrase.replaceAll("[^a-zA-Z 0-9]+", " ");
        cleanPhrase = cleanPhrase.replaceAll("\\s[\\s]*", " ");
        cleanPhrase = cleanPhrase.trim();
        return cleanPhrase;
    }

    public ArrayList<String> getKeywords(final String phrase) {
        final String cleanPhrase = cleanPhrase(phrase);
        ArrayList<String> keywordSet = new ArrayList<>(Arrays.asList(cleanPhrase.split(" ")));

        for (ListIterator<String> iter = keywordSet.listIterator(); iter.hasNext();) {
            String word = iter.next();
            if (isStepword(word)) {
                iter.remove();
            }
        }

        keywordSet.replaceAll(String::toLowerCase);
        keywordSet.replaceAll(String::trim);
        return keywordSet;
    }

    public ArrayList<String> keywordsWithout(ArrayList<String> keywordSet, String bannedKeyword) {
        for (ListIterator<String> iter = keywordSet.listIterator(); iter.hasNext();) {
            String word = iter.next();
            if (word.equals(bannedKeyword)) {
                iter.remove();
            }
        }
        return keywordSet;
    }
}
