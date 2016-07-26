package com.epam.am.text;

import com.epam.am.text.entity.Composite;
import com.epam.am.text.entity.Text;
import com.epam.am.text.parser.Parser;
import com.epam.am.text.parser.RegexParser;

public class Runner {
    public static void main(String[] args) {
        Parser parser = new RegexParser(RegexParser.DefaultConfig.PATTERN_MAP, RegexParser.DefaultConfig.COMPONENT_MAP);
        String testString = "There we. And another one is here. \nTest? Test!";
        Composite text = parser.parse(Text.class, testString, 0, testString.length());
        System.out.println(text);
        text.forEach(System.out::println);
    }
}
