package com.epam.am.text.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
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

    public <T extends Component> Iterator<T> deepIterator(Class<T> clazz) {
        if (componentList.isEmpty()) {
            return Collections.emptyIterator();
        }

        return new Iterator<T>() {
            private Iterator<E> wrappedIterator = componentList.iterator();
            private Iterator<T> innerIterator = null;
            private T next;

            @Override
            public boolean hasNext() {
                if (innerIterator != null) {
                    if (innerIterator.hasNext()) {
                        next = innerIterator.next();
                        return true;
                    } else {
                        innerIterator = null;
                    }
                }

                if (!wrappedIterator.hasNext()) return false;

                E tmpNext = wrappedIterator.next();
                if (tmpNext.getClass().isAssignableFrom(clazz)) {
                    next = (T) tmpNext;
                    return true;
                } else {
                    if (tmpNext instanceof AbstractComposite) {
                        innerIterator = ((AbstractComposite) tmpNext).deepIterator(clazz);
                        return hasNext();
                    } else {
                        return hasNext();
                    }
                }
            }

            @Override
            public T next() {
                return next;
            }
        };
    }
}
