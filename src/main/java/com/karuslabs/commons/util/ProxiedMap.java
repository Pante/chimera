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

import com.karuslabs.commons.annotations.Proxied;

import java.util.*;


public class ProxiedMap<K, V> implements Map<K, V> {
    
    protected Map<K, V> map;
    
    
    public ProxiedMap() {
        this(new HashMap<>());
    }
    
    public ProxiedMap(int capacity) {
        this(new HashMap<>(capacity));
    }
    
    public ProxiedMap(Map<K, V> map) {
        this.map = map;
    }
    
    
    @Override
    @Proxied
    public int size() {
        return map.size();
    }

    @Override
    @Proxied
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    @Proxied
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    @Proxied
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    @Proxied
    public V get(Object key) {
        return map.get(key);
    }

    @Override
    @Proxied
    public V put(K key, V value) {
        return map.put(key, value);
    }

    @Override
    @Proxied
    public V remove(Object key) {
        return map.remove(key);
    }

    @Override
    @Proxied
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    @Proxied
    public void clear() {
        map.clear();
    }

    @Override
    @Proxied
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    @Proxied
    public Collection<V> values() {
        return map.values();
    }

    @Override
    @Proxied
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }
    
}
