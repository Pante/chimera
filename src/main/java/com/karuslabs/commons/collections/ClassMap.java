/*
 * Copyright (C) 2017 Karus Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.karuslabs.commons.collections;

import java.util.*;
import java.util.Map.Entry;


/**
 * Represents a map with the value's class as the key. 
 * This is a single-value implementation of Josh Bloch's type-safe heterogeneous container.
 * 
 * @param <T> the type of mapped values
 */
public class ClassMap<T> implements Map<Class<? extends T>, T> {
    
    private Map<Class<? extends T>, T> map;
    
    
    /**
     * Creates a new, empty map with a backing {@link java.util.HashMap}.
     */
    public ClassMap() {
        this(new HashMap<>());
    }
    
    /**
     * Creates a new, empty map with a backing {@link java.util.HashMap} and an initial table size 
     * accommodating the specified number of elements without the need to dynamically resize.
     * 
     * @param capacity the initial capacity
     */
    public ClassMap(int capacity) {
        this(new HashMap<>(capacity));
    }
    
    /**
     * Creates a new, empty map with the backing map specified.
     * 
     * @param map the backing map
     */
    public ClassMap(Map<Class<? extends T>, T> map) {
        this.map = map;
    }

    

    @Override
    public void clear() {
        map.clear();
    }
    

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }


    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }
    
    @Override
    public Set<Entry<Class<? extends T>, T>> entrySet() {
        return map.entrySet();
    }
        
    @Override
    public T get(Object key) {
        return map.get(key);
    }
    
    /**
     * Returns the value to which the specified key is mapped, or <code>null</code> if this map contains no mapping for the key.
     * 
     * @param <U> the type of the value
     * @param type the class of the value the value is mapped to
     * @return the value to which the specified key is mapped, or <code>null</code> if this map contains no mapping for the key
     */
    public <U extends T> U get(Class<U> type) {
        return type.cast(map.get(type));
    }
    
    /**
     * Returns the value to which the specified key is mapped, or <code>null</code> if this map contains no mapping for the key.
     * 
     * @param <U> the type of the value
     * @param type the class of the value the value is mapped to
     * @param value the value to return if this map contains no mapping for the given key
     * @return the mapping for the key, if present; else the default value
     */
    public <U extends T> U getOrDefault(Class<U> type, U value) {
        T uncasted = map.get(type);
        if (uncasted != null) {
            return type.cast(uncasted);
            
        } else {
            return value;
        }
    }
    
    @Override
    public T put(Class<? extends T> key, T value) {
        return map.put(key, value);
    }
    
    @Override
    public void putAll(Map<? extends Class<? extends T>, ? extends T> map) {
        this.map.putAll(map);
    }


    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public Set<Class<? extends T>> keySet() {
        return map.keySet();
    }
    
    @Override
    public T remove(Object key) {
        return map.remove(key);
    }

    @Override
    public int size() {
        return map.size();
    }


    @Override
    public Collection<T> values() {
        return map.values();
    }

}
