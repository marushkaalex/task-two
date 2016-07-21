package com.epam.am.text.entity;

public class Symbol implements Leaf {
    private char value;

    public Symbol(char value) {
        this.value = value;
    }

    public void toPlainString(StringBuilder sb) {
        sb.append(value);
    }
}
