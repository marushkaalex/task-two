package com.epam.am.text.parser;

import com.epam.am.text.entity.Component;

public interface Parser {
    <T extends Component> T parse(Class<T> componentClass, String source, int start, int end);
}
