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
 * <p>
 * This class is a temporary workaround until Java 9 has reached general availability, after which this class will be rendered
 * obsolete by <a href = "http://openjdk.java.net/jeps/269">JEP 269</a> and subsequently be removed.
 */
public class Maps {
    
    /**
     * Returns a builder for constructing a <code>HashMap</code>.
     * 
     * @return a builder which constructs a HashMap
     */
    public static <K, V> Builder<K, V> builder() {
        return builder(new HashMap<>());
    }
    
    /**
     * Returns a builder for constructing a <code>HashMap</code> with the specified first key-value pair.
     * 
     * @return a builder which constructs a HashMap
     */
    public static <K, V> Builder<K, V> builder(K key, V value) {
        return builder().put(key, value);
    }
    
    /**
     * Returns a new builder for building the specified map.
     * 
     * @return a builder which will build the specified map
     */
    public static <K, V> Builder<K, V> builder(Map<K, V> map) {
        return new Builder(map);
    }
    
    
    /**
     * Represents a builder used to facilitate creation of <code>Map</code>s.
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
         * Maps the specified key to the specified value.
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
         * Copies all of the mappings from the specified map.
         * 
         * @param map the map which contains mappings to be stored
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
         * Returns a unmodifiable version of the map decorated via <code>Collections.unmodifiableMap(Map)</code>.
         * 
         * @return a unmodifiable version of the map
         */
        public Map<K, V> buildUnmodifiable() {
            return Collections.unmodifiableMap(map);
        }
        
    }
    
}
