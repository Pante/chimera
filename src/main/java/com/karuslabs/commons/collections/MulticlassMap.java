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


public class MulticlassMap<V> implements Map<MulticlassMap.Key<? extends V>, V> {
    
    private Map<Key<? extends V>, V> map;
    
    
    public MulticlassMap() {
        this(new HashMap<>());
    }
    
    public MulticlassMap(int capacity) {
        this(new HashMap<>(capacity));
    }
    
    public MulticlassMap(Map<Key<? extends V>, V> map) {
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
    public Set<Map.Entry<Key<? extends V>, V>> entrySet() {
        return map.entrySet();
    }
        
    @Override
    public V get(Object key) {
        return map.get(key);
    }
    
    public <U extends V> U get(Key<U> key) {
        return key.type.cast(map.get(key));
    }
    
    public <U extends V> U getOrDefault(Key<U> key, U value) {
        V uncasted = map.get(key);
        if (uncasted != null) {
            return key.type.cast(uncasted);
            
        } else {
            return value;
        }
    }
    
    @Override
    public V put(Key<? extends V> key, V value) {
        return map.put(key, value);
    }
    
    @Override
    public void putAll(Map<? extends Key<? extends V>, ? extends V> map) {
        this.map.putAll(map);
    }


    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public Set<Key<? extends V>> keySet() {
        return map.keySet();
    }
    
    @Override
    public V remove(Object key) {
        return map.remove(key);
    }

    @Override
    public int size() {
        return map.size();
    }


    @Override
    public Collection<V> values() {
        return map.values();
    }
    
    
    public static <T> Key<T> key(String name, Class<T> type) {
        return new Key(name , type);
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
