package com.epam.am.text.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public abstract class AbstractComposite<E extends Component> implements Composite<E> {
    private List<E> componentList = new ArrayList<>();

    public void add(E component) {
        componentList.add(component);
    }

    public boolean remove(E component) {
        return componentList.remove(component);
    }

    public void toPlainString(StringBuilder sb) {
        for (E item : componentList) {
            item.toPlainString(sb);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        componentList.forEach(sb::append);
        return sb.toString();
    }
}
