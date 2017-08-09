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


public class ClassMap<V> extends ProxiedMap<Class<? extends V>, V> {
    
    public ClassMap() {
        this(new HashMap<>());
    }
    
    public ClassMap(int capacity) {
        this(new HashMap<>(capacity));
    }
    
    public ClassMap(Map<Class<? extends V>, V> map) {
        super(map);
    }
    
    
    public <U extends V> U getInstance(Class<U> type) {
        return type.cast(map.get(type));
    }
    
    public <U extends V> U getInstanceOrDefault(Class<U> type, U value) {
        V uncasted = map.get(type);
        if (uncasted != null && uncasted.getClass() == type) {
            return type.cast(uncasted);
            
        } else {
            return value;
        }
    }
    
    
    public <U extends V> U putInstance(Class<U> type, U value) {
        return type.cast(map.put(type, value));
    }
    
}
