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

import com.karuslabs.annotations.Delegate;
import com.karuslabs.commons.util.Type;

import java.util.*;

import org.checkerframework.checker.nullness.qual.Nullable;


public interface ClassMap<T> {
    
    static <T> ClassMap<T> of() {
        return new HashClassMap<>();
    }
    
    static <T> ClassMap<T> of(int capacity) {
        return new HashClassMap<>(capacity);
    }
    
    static <T> @Delegate ClassMap<T> of(Map<Class<? extends T>, T> map) {
        return new ProxiedClassMap<>(map);
    }
    
    
    default <U extends T> boolean containsKey(Class<U> type) {
        return map().containsKey(type);
    }
    
    default <U extends T> boolean containsValue(@Nullable U value) {
        return map().containsValue(value);
    }
        
    
    default @Nullable <U extends T> U get(Class<U> type) {
        return (U) map().get(type);
    }
    
    default <U extends T> U getOrDefault(Class<U> type, U value) {
        var item = map().get(type);
        if (item != null && Type.box(type).isAssignableFrom(item.getClass())) {
            return (U) item;
            
        } else {
            return value;
        }
    }
    
    default <U extends T> @Nullable U put(Class<U> type, U value) {
        return (U) map().put(type, value);
    }
    
    default <U extends T> @Nullable U remove(Class<U> type) {
        return (U) map().remove(type);
    }
    
    
    public Map<Class<? extends T>, T> map();
    
}


class HashClassMap<T> extends HashMap<Class<? extends T>, T> implements ClassMap<T> {
    
    HashClassMap() {}
    
    HashClassMap(int capacity) {
        super(capacity);
    }
    
    @Override
    public Map<Class<? extends T>, T> map() {
        return this;
    }
    
}

@Delegate class ProxiedClassMap<T> implements ClassMap<T> {
    
    private final Map<Class<? extends T>, T> map;
    
    ProxiedClassMap(Map<Class<? extends T>, T> map) {
        this.map = map;
    }

    @Override
    public Map<Class<? extends T>, T> map() {
        return map;
    }
    
}