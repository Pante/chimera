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

import com.karuslabs.commons.collections.MultiClassMap.Key;

import junitparams.*;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static com.karuslabs.commons.collections.MultiClassMap.key;
import static org.junit.Assert.*;


@RunWith(JUnitParamsRunner.class)
public class MultiClassMapTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    private MultiClassMap<Object> map;
    private Key<Integer> key;
    
    
    public MultiClassMapTest() {
        map = new MultiClassMap<>();
        key = new MultiClassMap.Key<>("", Integer.class);
    }
    
    
    @Before
    public void setup() {
        map.map.clear();
    }
    
    
    @Test
    public void getInstance_ThrowsException() {
        exception.expect(ClassCastException.class);
        
        map.map.put(key, "");
        
        map.getInstance(key);
    }
    
    
    @Test
    @Parameters
    public void getInstanceOrDefault(Object object, int expected) {
        map.map.put(key, object);
        
        int returned = map.getInstanceOrDefault(key, 0);
        
        assertEquals(expected, returned);
    }
    
    protected Object[] parametersForGetInstanceOrDefault() {
        return new Object[] {
            new Object[] {5, 5},
            new Object[] {null, 0},
            new Object[] {"", 0}
        };
    }
    
    
    @Test
    public void putInstance() {
        map.putInstance(key, 1);
        
        assertTrue(map.map.containsKey(key));
    }
    
    
    @Test
    @Parameters(method = "parametersForKey")
    public void equals(Key<?> aKey, boolean isEqual) {
        assertEquals(isEqual, key.equals(aKey));
    }
    
    
    @Test
    @Parameters(method = "parametersForKey")
    public void hashCode(Key<?> aKey, boolean isEqual) {
        assertEquals(isEqual, key.hashCode() == aKey.hashCode());
    }
    
    
    protected Object[] parametersForKey() {
        return new Object[] {
            new Object[] {key("", Integer.class), true},
            new Object[] {key("", String.class), false},
            new Object[] {key("wrong", Integer.class), false},
            new Object[] {key("wrong", String.class), false}
        };
}
    
}
