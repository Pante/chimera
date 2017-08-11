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

import java.util.*;
import javax.annotation.Nullable;


public class TokenMap<V> extends ProxiedMap<TokenMap.Key<? extends V>, V> {
    
    public TokenMap() {
        this(new HashMap<>());
    }
    
    public TokenMap(int capacity) {
        this(new HashMap<>(capacity));
    }
    
    public TokenMap(Map<TokenMap.Key<? extends V>, V> map) {
        super(map);
    }
    
    
    public <U extends V> @Nullable U getInstance(Key<U> key) {
        return key.type.cast(map.get(key));
    }

    public <U extends V> @Nullable U getInstanceOrDefault(Key<U> key, U value) {
        V uncasted = map.get(key);
        if (uncasted != null && uncasted.getClass().equals(key.type)) {
            return key.type.cast(uncasted);

        } else {
            return value;
        }
    }

    
    public <U extends V> @Nullable U putInstance(Key<U> key, U value) {
        return key.type.cast(map.put(key, value));
    }
    
    
    public static <T> Key<T> key(String name, Class<T> type) {
        return new Key(name, type);
    }
    
    
    public static class Key<T> {
        
        private String name;
        private Class<T> type;
        
        
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
