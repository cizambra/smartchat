package com.pxw.smartchat.config.system;


import java.util.Properties;

public class NLPParser {
    private static final NLPParser instance = new NLPParser();
    private final Properties props = new Properties();

    private NLPParser() {
    }

    public static NLPParser getInstance() {
        return instance;
    }
}
