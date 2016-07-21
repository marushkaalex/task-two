package com.epam.am.text.entity;

public interface Composite<E extends Component> extends Component {
    void add(E component);

    boolean remove(E component);
}
