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
        patternMap.put(Paragraph.class, Pattern.compile("[^\\n]+?"));

        Map<Class<? extends Composite>, List<Class<? extends Component>>> componentMap = new HashMap<>();
        componentMap.put(Word.class, Collections.singletonList(WordSymbol.class));
        componentMap.put(Sentence.class, Arrays.asList(Word.class, PunctuationSymbol.class, WhitespaceSymbol.class));
        componentMap.put(Paragraph.class, Collections.singletonList(Sentence.class));

        Parser parser = new RegexParser(patternMap, componentMap);
        String testString = "There we have a sentence. And another one is here. Test? Test!";
        Paragraph paragraph = parser.parse(Paragraph.class, testString, 0, testString.length());
        System.out.println(paragraph);
        paragraph.forEach(System.out::println);
    }
}
