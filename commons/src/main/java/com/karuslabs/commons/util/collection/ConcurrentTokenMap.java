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

import com.karuslabs.commons.util.collection.TokenMap.Key;

import java.util.concurrent.*;


/**
 * A concurrent {@code TokenMap}.
 * 
 * @param <N> the type of the keys
 * @param <T> the type of the values 
 */
public interface ConcurrentTokenMap<N, T> extends TokenMap<N, T> {
    
    /**
     * Creates a {@code ConcurrentTokenMap}.
     * 
     * @param <N> the type of the keys
     * @param <T> the type of the values
     * @return a {@code ConcurrentTokenMap}
     */
    public static <N, T> ConcurrentTokenMap<N, T> of() {
        return new ConcurrentHashTokenMap<>();
    }
    
    /**
     * Creates a {@code ConcurrentTokenMap} with the given initial capacity.
     * 
     * @param <N> the type of the keys
     * @param <T> the type of the values
     * @param capacity the initial capacity
     * @return a {@code ConcurrentTokenMap}
     */
    public static <N, T> ConcurrentTokenMap<N, T> of(int capacity) {
        return new ConcurrentHashTokenMap<>(capacity);
    }
    
    /**
     * Creates a {@code ConcurrentTokenMap} backed by the given map.
     * 
     * @param <N> the type of the keys
     * @param <T> the type of the values
     * @param map the backing map
     * @return a {@code ConcurrentTokenMap}
     */
    public static <N, T> ConcurrentTokenMap<N, T> of(ConcurrentMap<Key<N, ? extends T>, T> map) {
        return new ConcurrentProxiedTokenMap<>(map);
    }
    
    
    @Override
    public default <U extends T> boolean containsKey(N name, Class<U> type) {
        return map().containsKey(ThreadLocalKey.KEY.get().set(name, type));
    }
    
    @Override
    public default <U extends T> U get(N name, Class<U> type) {
        return get((Key<N, U>) ThreadLocalKey.KEY.get().set(name, type));
    }
    
    @Override
    public default <U extends T> U getOrDefault(N name, Class<U> type, U value) {
        return getOrDefault((Key<N, U>) ThreadLocalKey.KEY.get().set(name, type), value);
    }
    
    @Override
    public default <U extends T> U remove(N name, Class<U> type) {
        return remove((Key<N, U>) ThreadLocalKey.KEY.get().set(name, type));
    }


    @Override
    public ConcurrentMap<Key<N, ? extends T>, T> map();
    
}


class ThreadLocalKey extends ThreadLocal<Key<Object, Object>> {
    
    static final ThreadLocalKey KEY = new ThreadLocalKey();
    
    
    @Override
    protected Key<Object, Object> initialValue() {
        return TokenMap.key(null, null);
    }
    
}


class ConcurrentHashTokenMap<N, T> extends ConcurrentHashMap<Key<N, ? extends T>, T> implements ConcurrentTokenMap<N, T> {
    
    ConcurrentHashTokenMap() {}
    
    ConcurrentHashTokenMap(int capacity) {
        super(capacity);
    }
    
    
    @Override
    public ConcurrentMap<Key<N, ? extends T>, T> map() {
        return this;
    }
    
}


class ConcurrentProxiedTokenMap<N, T> implements ConcurrentTokenMap<N, T> {
    
    ConcurrentMap<Key<N, ? extends T>, T> map;
    
    
    ConcurrentProxiedTokenMap(ConcurrentMap<Key<N, ? extends T>, T> map) {
        this.map = map;
    }
    
    
    @Override
    public ConcurrentMap<Key<N, ? extends T>, T> map() {
        return map;
    }
    
}
