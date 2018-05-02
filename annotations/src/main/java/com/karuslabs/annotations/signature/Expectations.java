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

import com.karuslabs.annotations.Immutable;

import com.sun.source.tree.Tree;

import java.util.*;
import static java.util.List.of;
import java.util.function.Predicate;


public abstract class Expectations<T extends Tree> extends ArrayList<Predicate<T>> {
    
    private static final @Immutable Expectations<? extends Tree> ANY = new Any();
    
    
    public static <T extends Tree> Expectations<T> any() {
        return (Expectations<T>) ANY;
    }
    
    
    public static <T extends Tree> Expectations<T> contains(Predicate<T>... expectations) {
        Contains<T> contains = new Contains(expectations.length);
        contains.addAll(of(expectations));
        return contains;
    }
    
    
    public static <T extends Tree> Expectations<T> exactly(Predicate<T>... expectations) {
        Exact<T> exact = new Exact(expectations.length);
        exact.addAll(of(expectations));
        return exact;
    }
    
    
    public Expectations(int size) {
        super(size);
    }

    
    public abstract boolean check(List<? extends T> actual);

    
    protected boolean test(List<? extends T> actual) {
        for (int i = 0; i < size(); i++) {
            if (!get(i).test(actual.get(i))) {
                return false;
            }
        }

        return true;
    }
    
    
    @Immutable
    static class Any<T extends Tree> extends Expectations<T> {

        Any() {
            super(0);
        }
        
        @Override
        public boolean check(List<? extends T> actual) {
            return true;
        }

        @Override
        public Object clone() {
            Any<T> any = (Any<T>) super.clone();
            any.addAll(this);
            return any;
        }
        
    }
    
    static class Contains<T extends Tree> extends Expectations<T> {

        public Contains(int size) {
            super(size);
        }

        @Override
        public boolean check(List<? extends T> actual) {
            return actual.size() >= size() && test(actual);
        }

        @Override
        public Object clone() {
            Contains<T> contains = (Contains<T>) super.clone();
            contains.addAll(this);
            return contains;
        }
        
    }
    
    static class Exact<T extends Tree> extends Expectations<T> {

        Exact(int size) {
            super(size);
        }

        
        @Override
        public boolean check(List<? extends T> actual) {
            return actual.size() == size() && test(actual);
        }

        @Override
        public Object clone() {
            Exact<T> exact = (Exact<T>) super.clone();
            exact.addAll(this);
            return exact;
        }
        
    }
    
}
