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


/**
 * Represents a map with the class of a value as the key and is an implementation of Josh Bloch's 
 * type-safe heterogeneous container.
 * 
 * Delegates most {@link java.util.Map} methods to an internal map.
 * 
 * @param <T> The lower bound
 */
public class ClassMap<T> implements Map<Class<? extends T>, T> {
    
    private Map<Class<? extends T>, T> map;
    
    
    /**
     * Constructs this with a {@link java.util.HashMap} as the internal map.
     */
    public ClassMap() {
        this(new HashMap<>());
    }
    
    /**
     * Constructs this with a {@link java.util.HashMap} as the internal map and the specified default capacity.
     * 
     * @param capacity The default capacity
     */
    public ClassMap(int capacity) {
        this(new HashMap<>(capacity));
    }
    
    /**
     * Constructs this with the specified map as the internal map.
     * 
     * @param map The specified internal map
     */
    public ClassMap(Map<Class<? extends T>, T> map) {
        this.map = map;
    }
    
    
    /**
     * Returns the casted value to which the specified key is mapped, or null if this map contains no mapping for the key.
     * 
     * @param <U> A type that extends the lower bound specified for this
     * @param type The object the key the value is mapped to and will be casted to
     * @return The casted value to which the specified key is mapped, or null if this map contains no mapping for the key
     */
    public <U extends T> U getCasted(Class<U> type) {
        return type.cast(map.get(type));
    }
    
    /**
     * Returns the casted value to which the specified key is mapped, or value if this map contains no mapping for the key.
     * 
     * @param <U> A type that extends the lower bound specified for this
     * @param type The object the key the value is mapped to and will be casted to
     * @param value the default mapping of the key
     * @return The casted value to which the specified key is mapped, or value if this map contains no mapping for the key
     */
    public <U extends T> U getCastedOrDefault(Class<U> type, U value) {
        T uncasted = map.get(type);
        if (uncasted != null) {
            return type.cast(map.get(type));
            
        } else {
            return value;
        }
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
    public T get(Object key) {
        return map.get(key);
    }

    @Override
    public T put(Class<? extends T> key, T value) {
        return map.put(key, value);
    }

    @Override
    public T remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends Class<? extends T>, ? extends T> map) {
        this.map.putAll(map);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<Class<? extends T>> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<T> values() {
        return map.values();
    }

    @Override
    public Set<Entry<Class<? extends T>, T>> entrySet() {
        return map.entrySet();
    }
    
}
