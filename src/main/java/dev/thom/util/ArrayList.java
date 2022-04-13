package dev.thom.util;

import java.util.Arrays;

public class ArrayList<T> implements ThomList<T> {

    private static int ARRAY_RESIZE_STEP = 10;

    private Object[] elements;
    private int currentIndex;

    public ArrayList() {
        this.elements = new Object[ARRAY_RESIZE_STEP];
        this.currentIndex = 0;
    }

    @Override
    public void add(T element) {

        if (currentIndex >= this.elements.length) {
            // time to resize the array
            this.elements = Arrays.copyOf(this.elements, this.elements.length + ARRAY_RESIZE_STEP);
        }

        this.elements[currentIndex] = element;

        this.currentIndex++;

    }

    @Override
    public T get(int index) {
       return (T) this.elements[index];
    }

    @Override
    public int size() {
        return this.currentIndex;
    }

    @Override
    public boolean isEmpty() {
        return this.currentIndex == 0;
    }
}
