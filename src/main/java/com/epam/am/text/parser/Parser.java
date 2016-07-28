package com.epam.am.text.parser;

import com.epam.am.text.entity.Component;
import com.epam.am.text.entity.Text;

public interface Parser {
    <T extends Component> T parse(Class<T> componentClass, String source);

    Text parse(String source);
}
