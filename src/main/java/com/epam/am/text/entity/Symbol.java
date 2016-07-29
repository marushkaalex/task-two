package com.epam.am.text.entity;

import com.epam.am.text.parser.ParsingException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class Symbol implements Leaf, Comparable<Symbol> {
    private static final Map<Character, Symbol> CACHE = new HashMap<>();

    private char value;

    public Symbol(char value) {
        this.value = value;
    }

    public void toPlainString(StringBuilder sb) {
        sb.append(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Symbol)) return false;

        Symbol symbol = (Symbol) o;

        return value == symbol.value;

    }

    @Override
    public int hashCode() {
        return (int) value;
    }

    @Override
    public int compareTo(Symbol o) {
        return Character.compare(value, o.value);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Symbol> T of(char value, Class<T> clazz) {
        Symbol symbol = CACHE.get(value);
        if (symbol == null) {
            try {
                symbol = clazz.getDeclaredConstructor(char.class).newInstance(value);
                CACHE.put(value, symbol);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new ParsingException(e);
            }
        }

        return (T) symbol;
    }
}
