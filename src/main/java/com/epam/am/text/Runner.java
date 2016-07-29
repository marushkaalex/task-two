package com.epam.am.text;

import com.epam.am.text.entity.Sentence;
import com.epam.am.text.entity.Text;
import com.epam.am.text.entity.Word;
import com.epam.am.text.entity.WordSymbol;
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
import java.util.*;

public class Runner {
    private static final Logger log = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) {
        Parser parser = new RegexParser();
        String testString = readFile("text.txt");
        Text text = parser.parse(testString);
        log.info(text.toString());

        task2(text);
        task3(text);
        task6(text);
    }

    private static String readFile(String fileName) {
        try {
            URI uri = Runner.class.getClassLoader().getResource(fileName).toURI();
            return new String(Files.readAllBytes(Paths.get(uri)), StandardCharsets.UTF_8);
        } catch (IOException | URISyntaxException e) {
            throw new RunnerException(e);
        }
    }

    private static void task2(Text text) {
        Iterator<Sentence> iterator = text.deepIterator(Sentence.class);
        Map<Sentence, Long> wordCountMap = new HashMap<>();
        while (iterator.hasNext()) {
            Sentence next = iterator.next();
            wordCountMap.put(next, next.getWordCount());
        }

        log.info("=== Task 2 ===");
        StringBuilder sb = new StringBuilder();
        wordCountMap
                .entrySet()
                .stream()
                .sorted((lhs, rhs) -> Long.compare(lhs.getValue(), rhs.getValue()))
                .map(i -> {
                    sb.setLength(0);
                    i.getKey().toPlainString(sb);
                    return String.format("Word count: %d; sentence: %s", i.getValue(), sb);
                })
                .forEach(log::info);
    }

    private static void task3(Text text) {
        log.info("=== Task 3 ===");
        Iterator<Sentence> sentenceIterator = text.deepIterator(Sentence.class);
        if (!sentenceIterator.hasNext()) {
            log.info("Text doesn't contain any sentence");
            return;
        }

        Sentence first = sentenceIterator.next();

        Set<Word> textWordSet = new HashSet<>();
        while (sentenceIterator.hasNext()) {
            Sentence sentence = sentenceIterator.next();
            Iterator<Word> wordIterator = sentence.deepIterator(Word.class);
            while (wordIterator.hasNext()) {
                textWordSet.add(wordIterator.next());
            }
        }

        Set<Word> resWordSet = new HashSet<>();
        Iterator<Word> firstSentenceWordIterator = first.deepIterator(Word.class);
        while (firstSentenceWordIterator.hasNext()) {
            Word word = firstSentenceWordIterator.next();
            if (!textWordSet.contains(word)) {
                resWordSet.add(word);
            }
        }

        StringBuilder sb = new StringBuilder();
        resWordSet.stream().forEach(i -> {
            sb.setLength(0);
            i.toPlainString(sb);
            log.info(sb.toString());
        });
    }

    private static void task6(Text text) {
        log.info("=== Task 6 ===");
        Iterator<Word> wordIterator = text.deepIterator(Word.class);
        Map<WordSymbol, Set<Word>> map = new HashMap<>();

        while (wordIterator.hasNext()) {
            Word next = wordIterator.next();
            Iterator<WordSymbol> symbolIterator = next.iterator();
            WordSymbol firstSymbol = symbolIterator.next();
            Set<Word> words = map.get(firstSymbol);

            if (words == null) {
                words = new HashSet<>();
                map.put(firstSymbol, words);
            }

            words.add(next);
        }

        StringBuilder sb = new StringBuilder();
        map
                .entrySet()
                .stream()
                .sorted((lhs, rhs) -> lhs.getKey().compareTo(rhs.getKey()))
                .map(i -> {
                    sb.setLength(0);
                    i.getValue().stream().forEach(j -> { j.toPlainString(sb); sb.append(' '); } );
                    return String.format("%s: %s", i.getKey(), sb);
                })
                .forEach(log::info);
    }
}
