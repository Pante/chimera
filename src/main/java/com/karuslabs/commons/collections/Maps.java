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


public class Maps {
    
    public static <K, V> Builder<K, V> builder() {
        return builder(new HashMap<>());
    }
    
    public static <K, V> Builder<K, V> builder(K key, V value) {
        return builder().put(key, value);
    }
    
    public static <K, V> Builder<K, V> builder(Map<K, V> map) {
        return new Builder(map);
    }
    
    
    public static class Builder<K, V> {
        
        private Map<K, V> map;
        
        
        protected Builder(Map<K, V> map) {
            this.map = map;
        }
        
        
        public Builder put(K key, V value) {
            map.put(key, value);
            return this;
        }
        
        public Builder putAll(Map<K, V> map) {
            this.map.putAll(map);
            return this;
        }
        
        
        public Map<K, V> build() {
            return map;
        }
                
        public Map<K, V> buildUnmodifiable() {
            return Collections.unmodifiableMap(map);
        }
        
    }
    
}
