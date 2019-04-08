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

import com.google.common.primitives.Primitives;

import java.util.*;


/**
 * A map that associates a value with the class of the value. The primitive and
 * corresponding boxed types are considered distinct. Thus different values can 
 * be mapped to a primitive and its boxed type.
 * 
 * @param <T> the type of the values
 */
public interface ClassMap<T> {
    
    /**
     * Creates a {@code ClassMap}.
     * 
     * @param <T> the parent type that all values extend
     * @return a {@code ClassMap}
     */
    public static <T> ClassMap<T> of() {
        return new HashClassMap<>();
    }
    
    /**
     * Creates a {@code ClassMap} with the given initial capacity.
     * 
     * @param <T> the parent type that all values extend
     * @param capacity the initial capacity
     * @return a {@code ClassMap}
     */
    public static <T> ClassMap<T> of(int capacity) {
        return new HashClassMap<>(capacity);
    }
    
    /**
     * Creates a {@code ClassMap} backed by the given map.
     * 
     * @param <T> the parent type that all values extend
     * @param map the backing map
     * @return a {@code ClassMap}
     */
    public static <T> ClassMap<T> of(Map<Class<? extends T>, T> map) {
        return new ProxiedClassMap<>(map);
    }
    
    
    /**
     * Returns {@code true} if this map contains a mapping for the given type.
     * 
     * @param <U> the type
     * @param type the type
     * @return {@code true} if this map contains a mapping for the given class
     */
    public default <U extends T> boolean containsKey(Class<U> type) {
        return map().containsKey(type);
    }
    
    /**
     * Returns {@code true} if this map contains the given value.
     * 
     * @param <U> the type of the value
     * @param value the value
     * @return {@code true} if this map contains the value
     */
    public default <U extends T> boolean containsValue(U value) {
        return map().containsValue(value);
    }
        
    
    /**
     * Returns the value to which the given type is mapped, or {@code null} if this
     * map contains no mapping for the key.
     * 
     * @param <U> the type of the value
     * @param type the type
     * @return the value associated with the given type, or {@code null} if this 
     *         map contains no mapping for the type
     */
    public default <U extends T> U get(Class<U> type) {
        return (U) map().get(type);
    }
    
    /**
     * Returns the value to which the given type is mapped, or {@code defaultValue}
     * if the map contains no mapping for the key.
     * 
     * @param <U> the type of the value
     * @param type the type
     * @param value the default value
     * @return the value to which the given type is mapped, or {@code defaultValue}
     *         if this map contains no mapping for the type
     */
    public default <U extends T> U getOrDefault(Class<U> type, U value) {
        var item = map().get(type);
        if (item != null && Primitives.wrap(type).isAssignableFrom(item.getClass())) {
            return (U) item;
            
        } else {
            return value;
        }
    }
    
    
    /**
     * Associates the given value with the given type. If this map previously contained
     * a mapping for the type, the old value is replaced and returned.
     * 
     * @param <U> the type of the value
     * @param type the type with which the value is to be associated
     * @param value the value to be associated with the type
     * @return the previous value associated with the type, or {@code null} if there 
     *         was no mapping for the key
     */
    public default <U extends T> U put(Class<U> type, U value) {
        return (U) map().put(type, value);
    }
    
    /**
     * Removes the mapping for type from this map if present. Returns the value
     * to which this map previously associated the key, or {@code null} if the map 
     * contains no mapping for the key.
     * 
     * @param <U> the type of the value
     * @param type the type whose mapping is to be removed
     * @return the previous value associated with the type, or {@code null} if there 
     *         was no mapping for the type
     */
    public default <U extends T> U remove(Class<U> type) {
        return (U) map().remove(type);
    }
    
    
    /**
     * Returns a {@code Map} view of this {@code ClassMap}. Modifications to the
     * map view will affect this {@code ClassMap}.
     * 
     * @return a {@code Map} view
     */
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

class ProxiedClassMap<T> implements ClassMap<T> {
    
    private Map<Class<? extends T>, T> map;
    
    ProxiedClassMap(Map<Class<? extends T>, T> map) {
        this.map = map;
    }

    @Override
    public Map<Class<? extends T>, T> map() {
        return map;
    }
    
}