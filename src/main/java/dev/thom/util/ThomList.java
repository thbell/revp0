package dev.thom.util;

public interface ThomList<T> {

    void add(T element);

    T get(int index);

    int size();

    boolean isEmpty();

}
