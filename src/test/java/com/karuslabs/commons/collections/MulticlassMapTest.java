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

import com.karuslabs.commons.collections.MulticlassMap.Key;

import java.util.concurrent.FutureTask;

import junitparams.*;

import org.junit.*;
import org.junit.runner.RunWith;

import static com.karuslabs.commons.collections.MulticlassMap.key;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;


@RunWith(JUnitParamsRunner.class)
public class MulticlassMapTest {
    
    private MulticlassMap<Object> map;
    private Key<FutureTask> key;
    private FutureTask value;
    
    
    public MulticlassMapTest() {
        map = new MulticlassMap<>();
        key = key("name", FutureTask.class);
        value = mock(FutureTask.class);
    }
    
    
    @Before
    public void setup() {
        map.clear();
    }
    
    
    @Test
    public void get() {
        map.put(key, value);
        FutureTask returned = map.get(key);
        
        assertEquals(value, returned);
    }
    
    
    @Test
    @Parameters
    public void getOrDefault(FutureTask value, FutureTask defaultValue, FutureTask expected) {
        map.put(key, value);
        FutureTask returned = map.getOrDefault(key, defaultValue);
        
        assertEquals(expected, returned);
    }
    
    protected Object[] parametersForGetOrDefault() {
        return new Object[] {
            new Object[] {value, null, value},
            new Object[] {null, value, value}
        };
    }
    
    
    @Test
    @Parameters
    public void equals(Key<?> aKey, boolean isEqual) {
        assertEquals(isEqual, key.equals(aKey));
    }
    
    protected Object[] parametersForEquals() {
        return new Object[] {
            new Object[] {key("name", FutureTask.class), true},
            new Object[] {key("name", Runnable.class), false},
            new Object[] {key("wrong-name", FutureTask.class), false},
            new Object[] {key("wrong-name", Runnable.class), false}
        };
    }
    
    
    @Test
    @Parameters
    public void hashCode(Key<?> aKey, boolean isEqual) {
        assertEquals(isEqual, key.hashCode() == aKey.hashCode());
    }
    
    protected Object[] parametersForHashCode() {
        return new Object[] {
            new Object[] {key("name", Runnable.class), false},
            new Object[] {key("wrong-name", FutureTask.class), false},
            new Object[] {key("wrong-name", Runnable.class), false},
            new Object[] {key("name", FutureTask.class), true}
        };
    }
    
}
