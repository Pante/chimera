/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.karuslabs.annotations.signature;

import com.karuslabs.annotations.Ignored;

import com.sun.source.tree.*;

import java.util.List;
import javax.annotation.Nullable;


@FunctionalInterface
public interface Sequence<T extends TreeVisitor<Boolean, ?>> {
    
    public static <T extends TreeVisitor<Boolean, ?>> Sequence<T> any() {
        return (Sequence<T>) AnySequence.ANY;
    }
            
    public static <T extends TreeVisitor<Boolean, ?>> Sequence<T> empty() {
        return (Sequence<T>) EmptySequence.EMPTY;
    }
    
    public static <T extends TreeVisitor<Boolean, V>, V> And<Sequence, V> of(T... visitors) {
        return new ExactSequence<>(visitors);
    }
    
    public static <T extends TreeVisitor<Boolean, V>, V> And<Sequence, V> contains(T... visitors) {
        return new PartialSequence<>(visitors);
    }
    
    
    public boolean match(List<? extends Tree> trees);
    
}


abstract class AbstractSequence<T extends TreeVisitor<Boolean, V>, V> implements Sequence<T>, And<Sequence, V> {
    
    T[] visitors;
    @Nullable V[] values;
    
    
    AbstractSequence(T[] visitors, V[] values) {
        this.visitors = visitors;
        this.values = values;
    }
    
    
    @Override
    public Sequence and(V... values) {
        if (visitors.length != values.length) {
            throw new IllegalArgumentException("Invalid number of values specified, number of types and classes must be the same");
        }
        
        this.values = values;
        return this;
    }
    
    @Override
    public Sequence get() {
        this.values = null;
        return this;
    }
    
}


class AnySequence<T extends TreeVisitor<Boolean, ?>> implements Sequence<T> {
    
    static final AnySequence<?> ANY = new AnySequence<>();
    
    @Override
    public boolean match(@Ignored List<? extends Tree> trees) {
        return true;
    }
    
}

class EmptySequence<T extends TreeVisitor<Boolean, ?>> implements Sequence<T> {
    
    static final EmptySequence<?> EMPTY = new EmptySequence<>();
    
    @Override
    public boolean match(List<? extends Tree> trees) {
        return trees.isEmpty();
    }
    
}


class ExactSequence<T extends TreeVisitor<Boolean, V>, V> extends AbstractSequence<T, V> {

    ExactSequence(T[] visitors) {
        super(visitors, null);
    }

    @Override
    public boolean match(List<? extends Tree> trees) {
        if (visitors.length != trees.size()) {
            return false;
        }
        
        if (values == null) {
            for (int i = 0; i < visitors.length; i++) {
                if (!trees.get(i).accept(visitors[i], null)) {
                    return false;
                }
            }
        } else {
            for (int i = 0; i < visitors.length; i++) {
                if (!trees.get(i).accept(visitors[i], values[i])) {
                    return false;
                }
            }
        }
        
        
        return true;
    }
    
}

class PartialSequence<T extends TreeVisitor<Boolean, V>, V> extends AbstractSequence<T, V> {

    PartialSequence(T[] visitors) {
        super(visitors, null);
    }

    @Override
    public boolean match(List<? extends Tree> trees) {
        if (visitors.length > trees.size()) {
            return false;
        }
        
        int j = 0;
        if (values == null) {
            for (int i = 0; i < trees.size() && j < visitors.length; i++) {
                if (trees.get(i).accept(visitors[j], null)) {
                    j++;
                } else {
                    j = 0;
                }
            }
        } else {
            for (int i = 0; i < trees.size() && j < visitors.length; i++) {
                if (trees.get(i).accept(visitors[j], values[j])) {
                    j++;
                } else {
                    j = 0;
                }
            }
        }
        
        return (j + 1) == visitors.length;
    }
    
}