package dev.hugog.minecraft.blockstreet.utils;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Implements a stack that has a maximum size.
 *
 * @param <T> the type of elements in the stack
 */
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

    /**
     * Convert the stack to a list.
     *
     * <p>The first elements of the list will be the elements on the top of the stack.
     *
     * @return a list containing the elements of the stack
     */
    public List<T> toList() {
        return Lists.reverse(new ArrayList<>(this));
    }

    /**
     * Create a new SizedStack from a list.
     *
     * <p>The elements of the list will be pushed onto the stack, respecting the maximum size.
     *
     * @param list the list to convert to a SizedStack
     * @return a new SizedStack containing the elements of the list
     */
    public SizedStack<T> fromList(List<T> list) {
        final SizedStack<T> stack = new SizedStack<>(maxSize);
        for (T t : list) {
            stack.push(t);
        }
        return stack;
    }

}