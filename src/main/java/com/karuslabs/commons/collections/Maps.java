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
 * Consists exclusively of static methods that facilitates creation of <code>Map</code>s.
 * This class is a temporary workaround until Java 9 and <a href = "http://openjdk.java.net/jeps/269">JEP 269</a>
 * has achieved general availability, after which this class may be removed.
 */
public class Maps {
    
    /**
     * Returns a new <code>Builder</code> for a <code>HashMap</code>.
     * 
     * @return a Builder which will create a HashMap
     */
    public static <K, V> Builder<K, V> builder() {
        return builder(new HashMap<>());
    }
    
    /**
     * Returns a new <code>Builder</code> for a <code>HashMap</code> with the first specified key-value pair.
     * 
     * @return a Builder which will create a HashMap
     */
    public static <K, V> Builder<K, V> builder(K key, V value) {
        return builder().put(key, value);
    }
    
    /**
     * Returns a new <code>Builder</code> for the map specified.
     * 
     * @return a Builder which will create the map specified
     */
    public static <K, V> Builder<K, V> builder(Map<K, V> map) {
        return new Builder(map);
    }
    
    
    /**
     * Represents a Builder used to facilitate creation of <code>Map</code>s.
     */
    public static class Builder<K, V> {
        
        private Map<K, V> map;
        
        
        /**
         * Constructs a <code>Builder</code> with the specified map.
         * 
         * @param map the map to be built
         */
        protected Builder(Map<K, V> map) {
            this.map = map;
        }
        
        
        /**
         * Associates the specified value with the specified key in the map to build.
         * 
         * @param key key with which the specified value is to be associated
         * @param value value to be associated with the specified key
         * @return this
         */
        public Builder put(K key, V value) {
            map.put(key, value);
            return this;
        }
        
        /**
         * Copies all of the mappings from the specified map to the map to build.
         * 
         * @param map mappings to be stored in this map
         * @return this
         */
        public Builder putAll(Map<K, V> map) {
            this.map.putAll(map);
            return this;
        }
        
        
        /**
         * @return the map
         */
        public Map<K, V> build() {
            return map;
        }
        
        /**
         * Returns a unmodifiable version of the map which is decorated by {@link Collections.unmodifiableMap(Map)}.
         * 
         * @return a unmodifiable version of the map
         */
        public Map<K, V> buildUnmodifiable() {
            return Collections.unmodifiableMap(map);
        }
        
    }
    
}
