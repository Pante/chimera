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


public class TokenMap<V> extends ProxiedMap<Key<? extends V>, V> {
    
    public TokenMap() {
        this(new HashMap<>());
    }
    
    public TokenMap(int capacity) {
        this(new HashMap<>(capacity));
    }
    
    public TokenMap(Map<Key<? extends V>, V> map) {
        super(map);
    }
    
    
    public <U extends V> @Nullable U getInstance(Key<U> key) {
        return (U) map.get(key);
    }

    public <U extends V> @Nullable U getInstanceOrDefault(Key<U> key, U value) {
        V uncasted = map.get(key);
        if (uncasted != null && key.type.isAssignableFrom(uncasted.getClass())) {
            return (U) uncasted;

        } else {
            return value;
        }
    }

    
    public <U extends V> @Nullable U putInstance(Key<U> key, U value) {
        return (U) map.put(key, value);
    }
    
    
    public static <T> @Immutable Key<T> key(String name, Class<T> type) {
        return new Key<>(name, type);
    }
    
    
    @Immutable
    @ValueBased
    public static class Key<T> {
        
        private final String name;
        private final Class<T> type;
        private final int hash;
        
        
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
        
        @Override
        public String toString() {
            return "Key[name: " + name + " class: " + type.getSimpleName() + "]";
        }
        
    }
    
}
