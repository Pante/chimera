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
        if (uncasted != null && uncasted.getClass().equals(type)) {
            return type.cast(uncasted);
            
        } else {
            return value;
        }
    }
    
}
