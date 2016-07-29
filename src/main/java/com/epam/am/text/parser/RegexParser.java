package com.epam.am.text.parser;

import com.epam.am.text.entity.*;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexParser implements Parser {
    private Map<Class<? extends Component>, Pattern> patternMap;
    private Map<Class<? extends Composite>, List<Class<? extends Component>>> componentMap;

    public RegexParser() {
        this.patternMap = DefaultConfig.PATTERN_MAP;
        this.componentMap = DefaultConfig.COMPONENT_MAP;
    }

    @Override
    public <T extends Component> T parse(Class<T> componentClass, String source) {
        return parse(componentClass, source, 0, source.length());
    }

    @Override
    public Text parse(String source) {
        return parse(Text.class, source);
    }

    @SuppressWarnings("unchecked")
    private  <T extends Component> T parse(Class<T> componentClass, String source, int start, int end) {
        try {
            if (Symbol.class.isAssignableFrom(componentClass)) {
                return (T) parseSymbol((Class<Symbol>) componentClass, source, start);
            }

            T t;
            t = componentClass.newInstance();
            int tmpStart = start;
            int tmpEnd = end;
            while (true) {
                if (tmpStart >= tmpEnd) {
                    break;
                }

                boolean check = false;

                for (Class innerComponentClass : componentMap.get(componentClass)) {
                    Composite composite = (Composite) t;
                    if (innerComponentClass.isAssignableFrom(Symbol.class)) {
                        Symbol symbol;
                        for (int i = start; i < end; i++) {
                            symbol = parseSymbol(innerComponentClass, source, start);
                            composite.add(symbol);
                        }
                    } else {
                        Pattern componentPattern = patternMap.get(innerComponentClass);
                        Matcher matcher = componentPattern.matcher(source);
                        boolean found = matcher.find(tmpStart);
                        if (!found || matcher.start() != tmpStart) continue;
                        tmpStart = matcher.start();
                        Component component = parse(innerComponentClass, source, tmpStart, matcher.end());
                        tmpStart = matcher.end();
                        composite.add(component);
                        check = true;
                        break;
                    }
                }

                if (!check)
                    throw new ParsingException(String.format(
                            "Can not parse '%s' into %s",
                            source.substring(tmpStart, tmpEnd),
                            componentClass
                    ));
            }

            return t;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ParsingException(e);
        }
    }

    private Symbol parseSymbol(Class<? extends Symbol> symbolClass, String source, int position) {
        return Symbol.of(source.charAt(position), symbolClass);
    }

    public static class DefaultConfig {
        public static final Map<Class<? extends Component>, Pattern> PATTERN_MAP;
        public static final Map<Class<? extends Composite>, List<Class<? extends Component>>> COMPONENT_MAP;

        static {
            PATTERN_MAP = new HashMap<>();
            PATTERN_MAP.put(Word.class, Pattern.compile("(?<!\\p{L})\\p{L}+"));
            PATTERN_MAP.put(Sentence.class, Pattern.compile(".+?[!.?\\n]+\\s*"));
            PATTERN_MAP.put(PunctuationSymbol.class, Pattern.compile("\\p{Punct}"));
            PATTERN_MAP.put(WordSymbol.class, Pattern.compile("\\p{L}"));
            PATTERN_MAP.put(WhitespaceSymbol.class, Pattern.compile("\\s"));
            PATTERN_MAP.put(Paragraph.class, Pattern.compile("[^\\n]+\\n*"));
            PATTERN_MAP.put(Text.class, Pattern.compile("^.*$"));

            COMPONENT_MAP = new HashMap<>();
            COMPONENT_MAP.put(Word.class, Collections.singletonList(WordSymbol.class));
            COMPONENT_MAP.put(Sentence.class, Arrays.asList(Word.class, PunctuationSymbol.class, WhitespaceSymbol.class));
            COMPONENT_MAP.put(Paragraph.class, Collections.singletonList(Sentence.class));
            COMPONENT_MAP.put(Text.class, Collections.singletonList(Paragraph.class));
        }
    }
}
