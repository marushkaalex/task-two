package com.epam.am.text;

import com.epam.am.text.entity.*;
import com.epam.am.text.parser.Parser;
import com.epam.am.text.parser.RegexParser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Runner {
    public static void main(String[] args) {
        Map<Class<? extends Component>, Pattern> patternMap = new HashMap<>();
        patternMap.put(Word.class, Pattern.compile("(?<!\\W)\\w+"));
        patternMap.put(Sentence.class, Pattern.compile("(?<!\\W).+?\\p{Punct}"));
        patternMap.put(PunctuationSymbol.class, Pattern.compile("\\p{Punct}"));
        patternMap.put(WordSymbol.class, Pattern.compile("\\p{Alpha}"));

        Map<Class<? extends Composite>, List<Class<? extends Component>>> componentMap = new HashMap<>();
        componentMap.put(Word.class, Arrays.asList(WordSymbol.class));
        componentMap.put(Sentence.class, Arrays.asList(Word.class, PunctuationSymbol.class));

        Parser parser = new RegexParser(patternMap, componentMap);
        String testString = "There we have a sentence.";
        Sentence sentence = parser.parse(Sentence.class, testString, 0, testString.length());
        System.out.println(sentence);
    }
}
