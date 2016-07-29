package com.epam.am.text.entity;

public abstract class Symbol implements Leaf, Comparable<Symbol> {
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
}
