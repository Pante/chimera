/* 
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
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
package com.karuslabs.commons.collection;

import com.karuslabs.commons.annotation.*;
import com.karuslabs.commons.collection.TokenMap.Key;

import java.util.*;
import javax.annotation.Nullable;


/**
 * A map which maps a {@code Key} which consists of a class and a identifier to an instance of that class.
 * Primitive classes and their respective wrapper classes are mapped to distinct instances.
 * For example: {@code map.put(new Key<>(int.class, "a"), 1)} and {@code map.put(new Key<>(Integer.class, "a"), 2)}.
 * 
 * @param <V> the common super-type that all entries must share.
 */
public class TokenMap<V> extends ProxiedMap<Key<? extends V>, V> {
    
    /**
     * Constructs a {@code TokenMap} backed by a {@code HashMap}.
     */
    public TokenMap() {
        this(new HashMap<>());
    }
    
    /**
     * Constructs a {@code TokenMap} backed by a {@code HashMap} with the specified default capacity.
     * 
     * @param capacity the default capacity of this map
     */
    public TokenMap(int capacity) {
        this(new HashMap<>(capacity));
    }
    
    /**
     * Constructs a {@code TokenMap} backed by the specified map.
     * 
     * @param map the map which all operations are delegated to
     */
    public TokenMap(Map<Key<? extends V>, V> map) {
        super(map);
    }
    
    
    /**
     * Returns the instance to which the specified {@code Key} is mapped, or {@code null} if this
     * map contains no mapping for the {@code Key}.
     * 
     * @param <U> the instance type
     * @param key the Key whose associated instance is to be returned
     * @return the instance to which the specified Key is mapped, or null if this map contains no mapping for the Key
     * @throws ClassCastException if the instance cannot be cast to the type of the specified Key
     */
    public <U extends V> @Nullable U getInstance(Key<U> key) {
        return (U) map.get(key);
    }
    
    /**
     * Returns the instance to which the specified {@code Key} is mapped, or {@code value} if this
     * map contains no mapping for the {@code Key}.
     * 
     * @param <U> the instance type 
     * @param key the Key whose associated instance is to be returned
     * @param value the default mapping of the Key
     * @return the instance to which the specified Key is mapped, or value if this map contains no mapping for the Key
     * @throws ClassCastException if the instance cannot be cast to the type of the specified Key
     */
    public <U extends V> @Nullable U getInstanceOrDefault(Key<U> key, U value) {
        V uncasted = map.get(key);
        if (uncasted != null && key.type.isAssignableFrom(uncasted.getClass())) {
            return (U) uncasted;

        } else {
            return value;
        }
    }

    /**
     * Associated the specified instance with the specified {@code Key}. If this map 
     * previously contained a mapping for the {@code Key}, the old instance is replaced by 
     * the specified instance.
     * 
     * @param <U> the instance type
     * @param key the Key with which the specified instance is to be associated
     * @param value the instance to be associated with the specified Key
     * @return the previous instance associated with the specified Key, or null if there was no mapping for the Key
     * @throws ClassCastException if the old instance associated cannot be cast to the type of the specified Key
     */
    public <U extends V> @Nullable U putInstance(Key<U> key, U value) {
        return (U) map.put(key, value);
    }
    
    
    /**
     * Returns a {@code Key} with the specified name and type.
     * 
     * @param <T> the type of the instance associated with this Key
     * @param name the name of the key used uniquely identify an instance
     * @param type the type of the instance associated with this Key
     * @return a Key
     */
    public static <T> @Immutable Key<T> key(String name, Class<T> type) {
        return new Key<>(name, type);
    }
    
    
    /**
     * Represents a key for {@link TokenMap} to which instances are mapped.
     * 
     * @param <T> the type of the instance the key is associated with
     */
    @Immutable
    @ValueBased
    public static class Key<T> {
        
        private final String name;
        private final Class<T> type;
        private final int hash;
        
        
        /**
         * Constructs a {@code Key} with the specified name and type.
         * 
         * @param name the name
         * @param type the type of the instance to associate this Key with
         */
        public Key(String name, Class<T> type) {
            this.name = name;
            this.type = type;
            this.hash = hash();
        }
        
        private int hash() {
            int hash = 5;
            hash = 53 * hash + Objects.hashCode(name);
            hash = 53 * hash + Objects.hashCode(type);
            return hash;
        }
        
        
        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
                
            } else if (object instanceof Key<?>) {
                Key key = (Key) object;
                return hash == key.hash && type == key.type && name.equals(key.name);
                
            } else {        
                return false;   
            }
        }

        @Override
        public int hashCode() {
            return hash;
        }
        
    }
    
}
