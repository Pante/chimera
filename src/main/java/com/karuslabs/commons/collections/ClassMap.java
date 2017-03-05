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
 * Represents a map with the values class as the key. 
 * This is a single-value implementation of Josh Bloch's type-safe heterogeneous container.
 * 
 * @param <V> the type of mapped values
 */
public class ClassMap<V> implements Map<Class<? extends V>, V> {
    
    private Map<Class<? extends V>, V> map;
    
    
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
    public ClassMap(Map<Class<? extends V>, V> map) {
        this.map = map;
    }
    
    

    @Override
    public int size() {
        return map.size();
    }
    

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
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
    public V get(Object key) {
        return map.get(key);
    }

    /**
     * Returns the value to which the specified key is mapped, or <code>null</code> if this map contains no mapping for the key.
     * 
     * @param <U> the type of the value
     * @param type the class of the value the value is mapped to
     * @return the value to which the specified key is mapped, or <code>null</code> if this map contains no mapping for the key
     */
    public <U extends V> U get(Class<U> type) {
        return type.cast(map.get(type));
    }
        
    @Override
    public V put(Class<? extends V> key, V value) {
        return map.put(key, value);
    }
        
    @Override
    public V remove(Object key) {
        return map.remove(key);
    }
    
    @Override
    public void putAll(Map<? extends Class<? extends V>, ? extends V> map) {
        this.map.putAll(map);
    }
    
    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<Class<? extends V>> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<Class<? extends V>, V>> entrySet() {
        return map.entrySet();
    }
    
    /**
     * Returns the value to which the specified key is mapped, or <code>null</code> if this map contains no mapping for the key.
     * 
     * @param <U> the type of the value
     * @param type the class of the value the value is mapped to
     * @param value the value to return if this map contains no mapping for the given key
     * @return the mapping for the key, if present; else the default value
     */
    public <U extends V> U getOrDefault(Class<U> type, U value) {
        V uncasted = map.get(type);
        if (uncasted != null) {
            return type.cast(uncasted);
            
        } else {
            return value;
        }
    }

}
