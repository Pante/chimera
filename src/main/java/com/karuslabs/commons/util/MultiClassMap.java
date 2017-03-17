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
package com.karuslabs.commons.util;

import java.util.*;


public class MultiClassMap<V> extends ProxiedMap<MultiClassMap.Key<? extends V>, V> {
    
    public MultiClassMap() {
        this(new HashMap<>());
    }
    
    public MultiClassMap(int capacity) {
        this(new HashMap<>(capacity));
    }
    
    public MultiClassMap(Map<MultiClassMap.Key<? extends V>, V> map) {
        super(map);
    }
    
    
    public <U extends V> U getInstance(Key<U> key) {
        return key.type.cast(map.get(key));
    }

    public <U extends V> U getInstanceOrDefault(Key<U> key, U value) {
        V uncasted = map.get(key);
        if (uncasted != null && uncasted.getClass().equals(key.type)) {
            return key.type.cast(uncasted);

        } else {
            return value;
        }
    }

    
    public <U extends V> U putInstance(Key<U> key, U value) {
        map.put(key, value);
        return value;
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
