package com.epam.am.text.entity;

public interface Composite<E extends Component> extends Component, Iterable<E>{
    void add(E component);

    boolean remove(E component);
}
