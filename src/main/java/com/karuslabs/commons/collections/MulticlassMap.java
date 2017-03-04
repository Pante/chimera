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
 * Represents a map with the value's class and a <code>string</code> as the key.
 * This is a multi-value implementation of Josh Bloch's type-safe heterogeneous container.
 * 
 * @param <T> the type of mapped values
 */
public class MulticlassMap<T> implements Map<MulticlassMap.Key<? extends T>, T> {
    
    private Map<Key<? extends T>, T> map;
    
    
    /**
     * Creates a new, empty map with a backing {@link java.util.HashMap}.
     */
    public MulticlassMap() {
        this(new HashMap<>());
    }
    
    /**
     * Creates a new, empty map with a backing {@link java.util.HashMap} and an initial table size 
     * accommodating the specified number of elements without the need to dynamically resize.
     * 
     * @param capacity the initial capacity
     */
    public MulticlassMap(int capacity) {
        this(new HashMap<>(capacity));
    }
    
    /**
     * Creates a new, empty map with the backing map specified.
     * 
     * @param map the backing map
     */
    public MulticlassMap(Map<Key<? extends T>, T> map) {
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
    public Set<Map.Entry<Key<? extends T>, T>> entrySet() {
        return map.entrySet();
    }
        
    @Override
    public T get(Object key) {
        return map.get(key);
    }
    
    /**
     * Returns the value to which the specified key is mapped, or <code>null</code> if this map contains no mapping for the key.
     * 
     * @param <U> the type of the key & value
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or <code>null</code> if this map contains no mapping for the key
     */
    public <U extends T> U get(Key<U> key) {
        return key.type.cast(map.get(key));
    }
    
    /**
     * Returns the value to which the specified key is mapped, or <code>null</code> if this map contains no mapping for the key.
     * 
     * @param <U> the type of the key & value
     * @param key the key whose associated value is to be returned
     * @param value the value to return if this map contains no mapping for the given key
     * @return the mapping for the key, if present; else the default value
     */
    public <U extends T> U getOrDefault(Key<U> key, U value) {
        T uncasted = map.get(key);
        if (uncasted != null) {
            return key.type.cast(uncasted);
            
        } else {
            return value;
        }
    }
    
    @Override
    public T put(Key<? extends T> key, T value) {
        return map.put(key, value);
    }
    
    @Override
    public void putAll(Map<? extends Key<? extends T>, ? extends T> map) {
        this.map.putAll(map);
    }


    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public Set<Key<? extends T>> keySet() {
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
    
    
    /**
     * Convenience method that creates a new key with the name and type specified.
     * 
     * @param <U> the type of the key
     * @param name the name of the key
     * @param type the type of the key
     * @return a new key with the name and type specified
     */
    public static <U> Key<U> key(String name, Class<U> type) {
        return new Key(name , type);
    }
    
    
    /**
     * Represents a key to which a value is associated with.
     * Equality is determined by the name and type of the key.
     * 
     * @param <U> the type of the key
     */
    public static class Key<U> {
        
        private String name;
        private Class<U> type;
        
        
        /**
         * Creates a new key with the name and type specified.
         * 
         * @param name the name of the key
         * @param type the type of the key
         */
        public Key(String name, Class<U> type) {
            this.name = name;
            this.type = type;
        }
        
        @Override
        public boolean equals(Object object) {
            if (object instanceof Key) {
                Key key = (Key) object;
                return this.name.equals(key.name) && this.type.equals(key.type);
            }
            
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 53 * hash + Objects.hashCode(this.name);
            hash = 53 * hash + Objects.hashCode(this.type);
            return hash;
        }
        
    }
    
}
