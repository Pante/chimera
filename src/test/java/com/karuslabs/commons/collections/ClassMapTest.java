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

import java.util.concurrent.*;

import junitparams.*;

import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;


@RunWith(JUnitParamsRunner.class)
public class ClassMapTest {
    
    private static Class<FutureTask> key = FutureTask.class;
    private static FutureTask value = mock(FutureTask.class);
    
    private ClassMap<Runnable> map;
    
    
    public ClassMapTest() {
        map = new ClassMap<>();
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

}
