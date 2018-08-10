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
package com.karuslabs.commons.util.collections;

import java.util.*;
import java.util.function.Supplier;

import static com.karuslabs.commons.util.collections.TokenProxiedMap.DEFAULT;


public interface TokenMap<T, K> {
    
    public static <T, K> TokenMap<T, K> of() {
        return new TokenHashMap<>();
    }
    
    public static <T, K> TokenMap<T, K> of(Map<Class<? extends T>, Map<K, T>> map) {
        return TokenMap.of(map, (Supplier<Map<K, T>>) DEFAULT);
    }
    
    public static <T, K> TokenMap<T, K> of(Map<Class<? extends T>, Map<K, T>> map, Supplier<Map<K, T>> supplier) {
        return new TokenProxiedMap<>(map, supplier);
    }
    
    
    public default <U extends T> U get(Class<? extends U> type, K key) {
        var map = map().get(type);
        return map != null ? (U) map.get(key) : null;
    }
    
    public default <U extends T> U getOrDefault(Class<? extends U> type, K key, U value) {
        var map = map().get(type);
        if (map != null) {
            T item = map.get(key);
            if (item != null && type.isAssignableFrom(item.getClass())) {
                return (U) item;
            }
        }
        
        return value;
    }

    public default <U extends T> U put(Class<? extends U> type, K key, U value) {
        var map = map().get(type);
        if (map == null) {
            map = new HashMap<>(1);
        }
        U item = (U) map.put(key, value);
        map().put(type, map);
        
        return item;
    }
    
    
    public Map<Class<? extends T>, Map<K, T>> map();
    
}


class TokenHashMap<T, K> extends HashMap<Class<? extends T>, Map<K, T>> implements TokenMap<T, K> {
    
    TokenHashMap() {}
    
    TokenHashMap(int capacity) {
        super(capacity);
    }
    
    
    @Override
    public Map<Class<? extends T>, Map<K, T>> map() {
        return this;
    }
    
    @Override
    public Object clone() {
        return super.clone();
    }
    
}

class TokenProxiedMap<T, K> implements TokenMap<T, K> {
    
    static final Supplier<?> DEFAULT = () -> new HashMap<>(1);
    
    private Map<Class<? extends T>, Map<K, T>> map;
    private Supplier<Map<K, T>> supplier;
    
    
    TokenProxiedMap(Map<Class<? extends T>, Map<K, T>> map, Supplier<Map<K, T>> supplier) {
        this.map = map;
        this.supplier = supplier;
    }
    
    
    @Override
    public <U extends T> U put(Class<? extends U> type, K key, U value) {
        var map = map().get(type);
        if (map == null) {
            map = supplier.get();
        }
        U item = (U) map.put(key, value);
        map().put(type, map);
        
        return item;
    }
    
    @Override
    public Map<Class<? extends T>, Map<K, T>> map() {
        return map;
    }
    
}