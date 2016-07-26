package com.epam.am.text.parser;

import com.epam.am.text.entity.Component;
import com.epam.am.text.entity.Composite;
import com.epam.am.text.entity.Symbol;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexParser implements Parser {
    private Map<Class<? extends Component>, Pattern> patternMap;
    private Map<Class<? extends Composite>, List<Class<? extends Component>>> componentMap;

    public RegexParser(
            Map<Class<? extends Component>, Pattern> patternMap,
            Map<Class<? extends Composite>, List<Class<? extends Component>>> componentMap
    ) {
        this.patternMap = patternMap;
        this.componentMap = componentMap;
    }

    public <T extends Component> T parse(Class<T> componentClass, String source, int start, int end) {

        try {
            if (Symbol.class.isAssignableFrom(componentClass)) {
                return componentClass.getDeclaredConstructor(char.class).newInstance(source.charAt(start));
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
                            symbol = ((Symbol) innerComponentClass.getDeclaredConstructor(char.class).newInstance(source.charAt(i)));
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
                    throw new RuntimeException(String.format("Can not parse: %s", source.substring(tmpStart, tmpEnd)));
            }

            return t;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
