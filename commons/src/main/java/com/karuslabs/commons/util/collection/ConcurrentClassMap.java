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
package com.karuslabs.commons.util.collection;

import java.util.concurrent.*;


/**
 * A concurrent {@code ClassMap}.
 * 
 * @param <T> the type of the values 
 */
public interface ConcurrentClassMap<T> extends ClassMap<T> {
    
    /**
     * Creates a {@code ConcurrentClassMap}.
     *  
     * @param <T> the type of the values
     * @return a {@code ConcurrentClassMap}
     */
    public static <T> ConcurrentClassMap<T> of() {
        return new ConcurrentHashClassMap<>();
    }
    
    /**
     * Creates a {@code ConcurrentClassMap} with the given initial capacity.
     * 
     * @param <T> the type of the values
     * @param capacity the initial capacity
     * @return a {@code ConcurrentClassMap}
     */
    public static <T> ConcurrentClassMap<T> of(int capacity) {
        return new ConcurrentHashClassMap<>(capacity);
    }
    
    /**
     * Creates a {@code ConcurrentClassMap} backed by the given map.
     * 
     * @param <T> the type of the values
     * @param map the backing map
     * @return a {@code ConcurrentClassMap}
     */
    public static <T> ConcurrentClassMap<T> of(ConcurrentMap<Class<? extends T>, T> map) {
        return new ConcurrentProxiedClassMap<>(map);
    }
    
    
    @Override
    public ConcurrentMap<Class<? extends T>, T> map();
    
}


class ConcurrentHashClassMap<T> extends ConcurrentHashMap<Class<? extends T>, T> implements ConcurrentClassMap<T> {

    ConcurrentHashClassMap() {}
    
    ConcurrentHashClassMap(int capacity) {
        super(capacity);
    }
    
    @Override
    public ConcurrentMap<Class<? extends T>, T> map() {
        return this;
    }
    
}

class ConcurrentProxiedClassMap<T> implements ConcurrentClassMap<T> {

    private ConcurrentMap<Class<? extends T>, T> map;
    
    
    ConcurrentProxiedClassMap(ConcurrentMap<Class<? extends T>, T> map) {
        this.map = map;
    }

    @Override
    public ConcurrentMap<Class<? extends T>, T> map() {
        return map;
    }
    
}
