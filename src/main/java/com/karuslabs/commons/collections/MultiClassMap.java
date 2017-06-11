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
 * Represents a multi-value implementation of the typesafe heterogeneous container pattern.
 * <p>
 * This collection maps a {@link MultiClassMap.Key} consisting of a class and identifying string to an instance of that class, 
 * Also contains typesafe operations for insertion and retrieval. A primitive type and its corresponding wrapper type may map to different values.
 * <p>
 * For more details, please read this article on <a href = "https://gerardnico.com/wiki/design_pattern/typesafe_heterogeneous_container">
 * Typesafe heterogeneous containers</a>.
 * 
 * @param <V> the common supertype which all entries must share
 */
public class MultiClassMap<V> extends ProxiedMap<MultiClassMap.Key<? extends V>, V> {
    
    /**
     * Constructs a <code>MultiClassMap</code> with a backing <code>HashMap</code>.
     */
    public MultiClassMap() {
        this(new HashMap<>());
    }
    
    /**
     * Constructs a <code>MultiClassMap</code> with a backing <code>HashMap</code> with the specified initial capacity.
     * 
     * @param capacity the initial capacity
     */
    public MultiClassMap(int capacity) {
        this(new HashMap<>(capacity));
    }
    
    /**
     * Constructs a <code>MultiClassMap</code> with the specified backing map.
     * 
     * @param map the backing map
     */
    public MultiClassMap(Map<MultiClassMap.Key<? extends V>, V> map) {
        super(map);
    }
    
    
    /**
     * Returns the instance mapped to the specified <code>Key</code> if present; else null.
     * 
     * @param <U> the type of the instance returned
     * @param key the Key whose associated instance is to be returned
     * @return the instance mapped to the specified Key if present; else null
     */
    public <U extends V> U getInstance(Key<U> key) {
        return key.type.cast(map.get(key));
    }
    
    /**
     * Returns the instance mapped to the specified <code>Key</code> if present; else the specified value.
     * 
     * @param <U> the type of the instance returned
     * @param key the Key whose associated instance is to be returned
     * @param value the default mapping for the specified Key
     * @return the instance mapped to the specified Key if present; else the specified value.
     */
    public <U extends V> U getInstanceOrDefault(Key<U> key, U value) {
        V uncasted = map.get(key);
        if (uncasted != null && uncasted.getClass().equals(key.type)) {
            return key.type.cast(uncasted);

        } else {
            return value;
        }
    }

    
    /**
     * Maps the specified <code>Key</code> to the specified instance of the class contained in the specified <code>Key</code>.
     * 
     * @param <U> the type of the instance returned
     * @param key the Key with which the specified instance is to be associated
     * @param value the instance to be associated with the specified Key
     */
    public <U extends V> void putInstance(Key<U> key, U value) {
        map.put(key, value);
    }
    
    
    /**
     * Convenience method which constructs a <code>Key</code> to be used in 
     * conjunction with the insertion and retrieval operations.
     * 
     * @param <T> the type of the instance associated with this Key
     * @param name the name used to uniquely identify this Key
     * @param type the class of the instance associated with this Key
     * @return a new Key
     */
    public static <T> Key<T> key(String name, Class<T> type) {
        return new Key(name, type);
    }
    
    
    /**
     * Represents a key which contains a identifying string and a class to which an instance of that class is mapped to.
     * <p>
     * Equality is determined by comparing the combined hashes of <code>name</code> and <code>type</code>.
     * <p>
     * For example, <code>new Key("A", Object.class).equals(new Key("A", Object.class));</code> will return true,
     * and conversely, <code>new Key("A", int.class).equals(new Key("A", Object.class));</code> will return false.
     * 
     * @param <T> the type of the instance to be associated with this key
     */
    public static class Key<T> {
        
        private String name;
        private Class<T> type;
        
        
        /**
         * Constructs a new key with the specified name and type.
         * 
         * @param name the name of the Key used to determine equality
         * @param type the type of the instance associated with this key which is also used to determine equality
         */
        public Key(String name, Class<T> type) {
            this.name = name;
            this.type = type;
        }
        
        
        @Override
        public boolean equals(Object object) {
            if (object instanceof Key) {
                Key key = (Key) object;
                return name.equals(key.name) && type.equals(key.type);
            }
            
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 53 * hash + Objects.hashCode(name);
            hash = 53 * hash + Objects.hashCode(type);
            return hash;
        }
        
    }
}
