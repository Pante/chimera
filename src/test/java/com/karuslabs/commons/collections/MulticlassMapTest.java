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

import org.junit.Test;
import org.junit.runner.RunWith;

import static com.karuslabs.commons.collections.MulticlassMap.key;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;


@RunWith(JUnitParamsRunner.class)
public class MulticlassMapTest {
    
    private static FutureTask value = mock(FutureTask.class);
    
    private MulticlassMap<Runnable> map;
    
    
    public MulticlassMapTest() {
        map = new MulticlassMap<>();
    }
    
    
    @Test
    public void get() {
        map.put(key("future", FutureTask.class), value);
        FutureTask returned = map.get(key("future", FutureTask.class));
        
        assertEquals(value, returned);
    }
    
    
    @Test
    @Parameters
    public void getOrDefault(FutureTask value, FutureTask defaultValue, FutureTask expected) {
        map.put(key("future", FutureTask.class), value);
        FutureTask returned = map.getOrDefault(key("future", FutureTask.class), defaultValue);
        
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
    public void keyEquals(Key<? extends Runnable> key1, Key<? extends Runnable> key2, boolean equals) {
        assertEquals(key1.equals(key2), equals);
    }
    
    protected Object[] parametersForKeyEquals() {
        Key<FutureTask> key = key("name", FutureTask.class);
        return new Object[] {
            new Object[] {key, key("name", Runnable.class), false},
            new Object[] {key, key("wrong-name", FutureTask.class), false},
            new Object[] {key, key("wrong-name", Runnable.class), false},
            new Object[] {key, key("name", FutureTask.class), true}
        };
    }
    
    
    @Test
    @Parameters
    public void keyHashCode(Key<? extends Runnable> key1, Key<? extends Runnable> key2, boolean equals) {
        assertEquals(key1.hashCode() == key2.hashCode(), equals);
    }
    
    protected Object[] parametersForKeyHashCode() {
        Key<FutureTask> key = key("name", FutureTask.class);
        return new Object[] {
            new Object[] {key, key("name", Runnable.class), false},
            new Object[] {key, key("wrong-name", FutureTask.class), false},
            new Object[] {key, key("wrong-name", Runnable.class), false},
            new Object[] {key, key("name", FutureTask.class), true}
        };
    }
    
}
