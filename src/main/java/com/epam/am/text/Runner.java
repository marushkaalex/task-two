package com.epam.am.text;

import com.epam.am.text.entity.*;
import com.epam.am.text.parser.Parser;
import com.epam.am.text.parser.RegexParser;

import java.util.*;
import java.util.regex.Pattern;

public class Runner {
    public static void main(String[] args) {
        Map<Class<? extends Component>, Pattern> patternMap = new HashMap<>();
        patternMap.put(Word.class, Pattern.compile("(?<!\\w)\\w+"));
        patternMap.put(Sentence.class, Pattern.compile(".+?[!.?]+\\s*"));
        patternMap.put(PunctuationSymbol.class, Pattern.compile("\\p{Punct}"));
        patternMap.put(WordSymbol.class, Pattern.compile("\\p{Alpha}"));
        patternMap.put(WhitespaceSymbol.class, Pattern.compile("\\s"));
        patternMap.put(Paragraph.class, Pattern.compile("[^\\n]+\\n*"));
        patternMap.put(Text.class, Pattern.compile("^.*$"));

        Map<Class<? extends Composite>, List<Class<? extends Component>>> componentMap = new HashMap<>();
        componentMap.put(Word.class, Collections.singletonList(WordSymbol.class));
        componentMap.put(Sentence.class, Arrays.asList(Word.class, PunctuationSymbol.class, WhitespaceSymbol.class));
        componentMap.put(Paragraph.class, Collections.singletonList(Sentence.class));
        componentMap.put(Text.class, Collections.singletonList(Paragraph.class));

        Parser parser = new RegexParser(patternMap, componentMap);
        String testString = "There we. And another one is here. \nTest? Test!";
        Composite text = parser.parse(Text.class, testString, 0, testString.length());
        System.out.println(text);
        text.forEach(System.out::println);
    }
}
