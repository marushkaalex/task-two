package com.epam.am.text;

import com.epam.am.text.entity.*;
import com.epam.am.text.parser.Parser;
import com.epam.am.text.parser.RegexParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class Runner {
    private static final Logger log = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) {
        Parser parser = new RegexParser(RegexParser.DefaultConfig.PATTERN_MAP, RegexParser.DefaultConfig.COMPONENT_MAP);
        String testString = "There we. And another one is here. \nTest? Test!";
        Text text = parser.parse(testString);
        log.info(text.toString());
        text.forEach(i -> log.info(i.toString()));

        Iterator<?> iterator = text.iterator(PunctuationSymbol.class);
        while (iterator.hasNext()) {
            log.info(iterator.next().toString());
        }
    }
}
