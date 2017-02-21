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


public class ClassMap<T> implements Map<Class<? extends T>, T> {
    
    private Map<Class<? extends T>, T> map;
    
    
    public ClassMap() {
        this(new HashMap<>());
    }
    
    public ClassMap(int capacity) {
        this(new HashMap<>(capacity));
    }
    
    public ClassMap(Map<Class<? extends T>, T> map) {
        this.map = map;
    }
    
    
    public <U extends T> U getCasted(Class<U> type) {
        return type.cast(map.get(type));
    }
    
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
