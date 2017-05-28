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
 * Represents a decorator for <code>ProxiedMap</code> which maps a type to an instance.
 * <p>
 * May contain a different value for a primitive type and its wrapper type.
 * For more detail, please read this article on <a href = "https://gerardnico.com/wiki/design_pattern/typesafe_heterogeneous_container">
 * Typesafe heterogeneous containers</a>.
 * 
 * @param <V> the common supertype which all entries must share
 */
public class ClassMap<V> extends ProxiedMap<Class<? extends V>, V> {
    
    /**
     * Constructs a <code>ClassMap</code> with a backing <code>HashMap</code>.
     */
    public ClassMap() {
        this(new HashMap<>());
    }
    
    /**
     * Constructs a <code>ClassMap</code> with a backing <code>HashMap</code> and specified initial capacity.
     * 
     * @param capacity the initial capacity
     */
    public ClassMap(int capacity) {
        this(new HashMap<>(capacity));
    }
    
    /**
     * Constructs a <code>ClassMap</code> with the backing map specified.
     * 
     * @param map the backing map
     */
    public ClassMap(Map<Class<? extends V>, V> map) {
        super(map);
    }
    
    
    /**
     * Returns the instance mapped to the specified <code>Class</code>, or null if there is no instance mapped to the <code>Class</code>.
     * 
     * @param type the Class whose associated instance is to be returned
     * @return the instance to which the specified Class is mapped, or null if there is no instance mapped to the Class
     */
    public <U extends V> U getInstance(Class<U> type) {
        return type.cast(map.get(type));
    }
    
    /**
     * Returns the instance mapped to the specified <code>Class</code>, or <code>value</code> if there is no instance mapped to the <code>Class</code>.
     * 
     * @param type the Class whose associated instance is to be returned
     * @param value the default mapping of the Class
     * @return the instance mapped to the specified <code>Class</code>, or <code>value</code> if there is no instance mapped to the <code>Class</code>.
     */
    public <U extends V> U getInstanceOrDefault(Class<U> type, U value) {
        V uncasted = map.get(type);
        if (uncasted != null && uncasted.getClass() == type) {
            return type.cast(uncasted);
            
        } else {
            return value;
        }
    }
    
    
    /**
     * Associates the specified instance with the specified <code>Class</code> in this map.
     * 
     * @param type the Class with which the specified instance is to be associated
     * @param value the instance to be associated with the specified Class
     */
    public <U extends V> void putInstance(Class<U> type, U value) {
        map.put(type, value);
    }
    
}
