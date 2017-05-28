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
 * Represents a decorator for <code>ProxiedMap</code> which maps a {@link MultiClassMap.Key} to an instance of the keys type.
 * May contain a different value for a primitive type and its wrapper type.
 * For more detail, please read this article on <a href = "https://gerardnico.com/wiki/design_pattern/typesafe_heterogeneous_container">
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
     * Constructs a <code>MultiClassMap</code> with a backing <code>HashMap</code> and specified initial capacity.
     * 
     * @param capacity the initial capacity
     */
    public MultiClassMap(int capacity) {
        this(new HashMap<>(capacity));
    }
    
    /**
     * Constructs a <code>MultiClassMap</code> with the backing map specified.
     * 
     * @param map the backing map
     */
    public MultiClassMap(Map<MultiClassMap.Key<? extends V>, V> map) {
        super(map);
    }
    
    
    /**
     * Returns the instance mapped to the specified <code>Key</code>, or null if there is no instance mapped to the <code>Key</code>.
     * 
     * @param <U> the type of the instance returned
     * @param key the Key whose associated instance is to be returned
     * @return the instance to which the specified Class is mapped, or null if there is no instance mapped to the Key
     */
    public <U extends V> U getInstance(Key<U> key) {
        return key.type.cast(map.get(key));
    }
    
    /**
     * Returns the instance mapped to the specified <code>Key</code>, or <code>value</code> if there is no instance mapped to the <code>Key</code>.
     * 
     * @param <U> the type of the instance returned
     * @param key the Key whose associated instance is to be returned
     * @param value the default mapping of the Key
     * @return the instance mapped to the specified <code>Key</code>, or <code>value</code> if there is no instance mapped to the <code>Key</code>.
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
     * 
     * @param <U>
     * @param key
     * @param value
     * @return 
     */
    public <U extends V> void putInstance(Key<U> key, U value) {
        map.put(key, value);
    }
    
    
    /**
     * 
     * @param <T>
     * @param name
     * @param type
     * @return 
     */
    public static <T> Key<T> key(String name, Class<T> type) {
        return new Key(name, type);
    }
    
    
    /**
     * 
     * 
     * @param <T> 
     */
    public static class Key<T> {
        
        private String name;
        private Class<T> type;
        
        
        /**
         * 
         * 
         * @param name
         * @param type 
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
