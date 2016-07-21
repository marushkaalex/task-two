package com.epam.am.text.parser;

import com.epam.am.text.entity.Component;
import com.epam.am.text.entity.Composite;
import com.epam.am.text.entity.Symbol;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexParser implements Parser {
    private Map<Class<? extends Component>, Pattern> patternMap;
    private Map<Class<? extends Composite>, Class<? extends Component>> componentMap;

    public RegexParser(
            Map<Class<? extends Component>, Pattern> patternMap,
            Map<Class<? extends Composite>, Class<? extends Component>> componentMap
    ) {
        this.patternMap = patternMap;
        this.componentMap = componentMap;
    }

    public <T extends Composite> T parse(Class<T> compositeClass, String source, int start, int end) {
        T t;

        try {
            t = compositeClass.newInstance();
            Class componentClass = componentMap.get(compositeClass);

            if (componentClass == Symbol.class) {
                Symbol symbol;
                for (int i = start; i < end; i++) {
                    symbol = new Symbol(source.charAt(i));
                    t.add(symbol);
                }
            } else {
                Pattern componentPattern = patternMap.get(componentClass);
                Matcher matcher = componentPattern.matcher(source);
                while (matcher.find()) {
                    Component component = parse(componentClass, source, matcher.start(), matcher.end());
                    t.add(component);
                }
            }

            return t;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
