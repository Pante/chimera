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


/**
 * A map which maps a class to an instance of that class.
 * Primitive classes and their respective wrapper classes are mapped to distinct instances.
 * For example, {@code map.put(int.class, 1} and {@code map.put(Integer.class, 2}.
 * 
 * @param <V> the common super-type that all entries must share.
 */
public class ClassMap<V> extends ProxiedMap<Class<? extends V>, V> {
    
    /**
     * Constructs a {@code ClassMap} backed by a {@code HashMap}.
     */
    public ClassMap() {
        this(new HashMap<>());
    }
    
    /**
     * Constructs a {@code ClassMap} backed by a {@code HashMap} with the specified default capacity.
     * 
     * @param capacity the default capacity of this map
     */
    public ClassMap(int capacity) {
        this(new HashMap<>(capacity));
    }
    
    /**
     * Constructs a {@code ClassMap} backed by the specified map.
     * 
     * @param map the map which all operations are delegated to
     */
    public ClassMap(Map<Class<? extends V>, V> map) {
        super(map);
    }
    
    
    /**
     * Returns the instance to which the specified class is mapped, or {@code null} if this
     * map contains no mapping for the class.
     * 
     * @param <U> the instance type
     * @param type the class whose associated instance is to be returned
     * @return the instance to which the specified class is mapped, or null if this map contains no mapping for the class
     * @throws ClassCastException if the instance cannot be cast to the specified class
     */
    public <U extends V> @Nullable U getInstance(Class<U> type) {
        return (U) map.get(type);
    }
    
    /**
     * Returns the instance to which the specified class is mapped, or {@code value} if this
     * map contains no mapping for the class.
     * 
     * @param <U> the instance type 
     * @param type the class whose associated instance is to be returned
     * @param value the default mapping of the key
     * @return the instance to which the specified class is mapped, or value if this map contains no mapping for the class
     * @throws ClassCastException if the instance cannot be cast to the specified class
     */
    public <U extends V> U getInstanceOrDefault(Class<U> type, U value) {
        V uncasted = map.get(type);
        if (uncasted != null && type.isAssignableFrom(uncasted.getClass())) {
            return (U) uncasted;
            
        } else {
            return value;
        }
    }
    
    
    /**
     * Associated the specified instance with the specified key. If this map 
     * previously contained a mapping for the class, the old instance is replaced by 
     * the specified instance.
     * 
     * @param <U> the instance type
     * @param type class with which the specified instance is to be associated
     * @param value instance to be associated with the specified class
     * @return the previous instance associated with the specified class, or {@code null} if there was no mapping for the class
     * @throws ClassCastException if the old instance associated cannot be cast to the specified class
     */
    public <U extends V> @Nullable U putInstance(Class<U> type, U value) {
        return (U) map.put(type, value);
    }
    
}
