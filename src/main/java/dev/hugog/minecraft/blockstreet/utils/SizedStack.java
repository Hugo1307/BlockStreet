package dev.hugog.minecraft.blockstreet.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class SizedStack<T> extends Stack<T> {

    private final int maxSize;

    public SizedStack(int size) {
        super();
        this.maxSize = size;
    }

    @Override
    public T push(T object) {
        // If the stack is too big, remove elements until it's the right size.
        while (this.size() >= maxSize) {
            this.remove(0);
        }
        return super.push(object);
    }

    // Inverts the list so that the first elements of the list are the most recent ones.
    public List<T> toList() {
        List<T> list = new ArrayList<>(this);
        Collections.reverse(list);
        return list;
    }

    public SizedStack<T> fromList(List<T> list) {

        final SizedStack<T> stack = new SizedStack<>(maxSize);

        // Reverse the list because the first elements in the list are the most recent ones.
        // Therefore, we need to invert it so that the first elements are pushed in last, i.e.,
        // become the most recent.
        Collections.reverse(list);
        for (T t : list) stack.push(t);

        return stack;

    }

}