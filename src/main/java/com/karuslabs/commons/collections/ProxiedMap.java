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
 * Represents a map which delegates method calls to a backing map.
 * <p>
 * Subclasses should override methods to modify the behaviour as per the decorator pattern.
 * 
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public abstract class ProxiedMap<K, V> implements Map<K, V> {
    
    /**
     * The backing map, exposed to facilitate subclassing.
     */
    protected Map<K, V> map;
    
    
    /**
    * Constructs a <code>ProxiedMap</code> with a backing <code>HashMap</code>.
    */
    public ProxiedMap() {
        this(new HashMap<>());
    }
    
    /**
     * Constructs a <code>ProxiedMap</code> with a backing <code>HashMap</code> with the specified initial capacity.
     * 
     * @param capacity the initial capacity
     */
    public ProxiedMap(int capacity) {
        this(new HashMap<>(capacity));
    }
    
    /**
     * Constructs a <code>ProxiedMap</code> with the specified backing map.
     * 
     * @param map the backing map
     */
    public ProxiedMap(Map<K, V> map) {
        this.map = map;
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
    public V get(Object key) {
        return map.get(key);
    }

    @Override
    public V put(K key, V value) {
        return map.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }
    
}
