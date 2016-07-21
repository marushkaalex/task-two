package com.epam.am.text.parser;

import com.epam.am.text.entity.Composite;

public interface Parser {
    <T extends Composite> T parse(Class<T> compositeClass, String source, int start, int end);
}
