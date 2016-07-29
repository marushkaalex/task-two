package com.epam.am.text;

import com.epam.am.text.entity.Sentence;
import com.epam.am.text.entity.Text;
import com.epam.am.text.parser.Parser;
import com.epam.am.text.parser.RegexParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Runner {
    private static final Logger log = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) {
        Parser parser = new RegexParser();
        String testString = readFile("text.txt");
        Text text = parser.parse(testString);
        log.info(text.toString());

        task2(text);
    }

    private static String readFile(String fileName) {
        try {
            URI uri = Runner.class.getClassLoader().getResource(fileName).toURI();
            return new String(Files.readAllBytes(Paths.get(uri)), StandardCharsets.UTF_8);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static void task2(Text text) {
        Iterator<Sentence> iterator = text.deepIterator(Sentence.class);
        Map<Sentence, Long> wordCountMap = new HashMap<>();
        while (iterator.hasNext()) {
            Sentence next = iterator.next();
            wordCountMap.put(next, next.getWordCount());
        }

        wordCountMap
                .entrySet()
                .stream()
                .sorted((lhs, rhs) -> Long.compare(lhs.getValue(), rhs.getValue()))
                .map(i -> String.format("Word count: %d; sentence: %s", i.getValue(), i.getKey()))
                .forEach(log::info);
    }
}
