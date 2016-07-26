package com.epam.am.text.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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

    @Override
    public Iterator<E> iterator() {
        return componentList.iterator();
    }

    public <T extends Component> Iterator<T> iterator(Class<T> clazz) {
        return flatten().stream()
                .filter(i -> clazz.isAssignableFrom(i.getClass()))
                .map(i -> (T) i)
                .iterator();
    }

    public List<? super Component> flatten() {
        List<? super Component> resList = new ArrayList<>();
        flatten(this, resList);
        return resList;
    }

    private void flatten(Iterable<? extends Component> iterable, List<? super Component> resList) {
        StreamSupport
                .stream(iterable.spliterator(), false)
                .forEach(component -> {
                    resList.add(component);
                    if (component instanceof Composite) {
                        Composite composite = (Composite) component;
                        flatten(composite, resList);
                    }
                });
    }
}
