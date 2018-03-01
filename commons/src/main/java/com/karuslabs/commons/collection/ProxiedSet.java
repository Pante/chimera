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
package com.karuslabs.commons.collection;

import java.util.*;


/**
 * A set that delegates all operations to another set.
 * Extending classes should override operations to modify the behaviour of this set
 * in support of the <a href = "https://en.wikipedia.org/wiki/Decorator_pattern">Decorator pattern</a>.
 * 
 * @param <T>  the type of elements maintained by this set
 */
public class ProxiedSet<T> implements Set<T> {
    
    /**
     * Backing set which all operations are delegated to.
     */
    protected Set<T> set;

    
    /**
     * Constructs a {@code ProxiedSet} backed by the specified set.
     * 
     * @param set the backing set which all operations are delegated to
     */
    public ProxiedSet(Set<T> set) {
        this.set = set;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return set.size();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(Object o) {
        return set.contains(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<T> iterator() {
        return set.iterator();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] toArray() {
        return set.toArray();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T[] toArray(T[] a) {
        return set.<T>toArray(a);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(T e) {
        return set.add(e);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(Object o) {
        return set.remove(o);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        return set.containsAll(c);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(Collection<? extends T> c) {
        return set.addAll(c);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        return set.retainAll(c);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        return set.removeAll(c);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        set.clear();
    }
    
}
