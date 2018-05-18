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

import com.sun.source.tree.Tree;

import java.util.*;
import java.util.function.Predicate;
import javax.annotation.Nullable;


public abstract class Expectations<T extends Tree> {
    
    public static final Expectations ANY = new Any();
    
    public static <T extends Tree> Expectations<T> exactly(Predicate<T>... predicates) {
        return new Exact(predicates);
    }
     
    
    protected final Predicate<T>[] predicates;
    private @Nullable List<T> actual;
    
    
    public Expectations(Predicate<T>... predicates) {
        this.predicates = predicates;
    }
    
    
    public abstract boolean check(List<? extends T> trees);
    
    public boolean check() {
        boolean checked = check(lazy());
        lazy().clear();
        return checked;
    }
    
    
    public Expectations include(T tree) {
        lazy().add(tree);
        return this;
    }
    
    protected List<T> lazy() {
        if (actual == null) {
            actual = new ArrayList<>();
        }
        
        return actual;
    }
    
}


class Any<T extends Tree> extends Expectations<T> {    
    
    @Override
    public boolean check(List<? extends T> trees) {
        return true;
    }

    @Override
    public boolean check() {
        return true;
    }
    
    @Override
    public Expectations include(T tree) {
        return this;
    }
    
}


class Exact<T extends Tree> extends Expectations<T> {
    
    Exact(Predicate<T>... predicates) {
        super(predicates);
    }
    
    
    @Override
    public boolean check(List<? extends T> trees) {
        if (trees.size() != predicates.length) {
            return false;
        }
        
        for (int i = 0; i < predicates.length; i++) {
            if (!predicates[i].test(trees.get(i))) {
                return false;
            }
        }

        return true;
    }
    
}

class Sequence<T extends Tree> extends Expectations<T> {
    
    Sequence(Predicate<T>... predicates) {
        super(predicates);
    }

    @Override
    public boolean check(List<? extends T> trees) {
        int size = trees.size();
        if (trees.size() > predicates.length) {
            return false;
        }
        
        int j = 0;
        for (int i = 0; i < predicates.length && j < size; i++) {
            if (predicates[i].test(trees.get(j))) {
                j++;
                
            } else {
                j = 0;
            }
        }
        
        return j == size;
    }
    
}