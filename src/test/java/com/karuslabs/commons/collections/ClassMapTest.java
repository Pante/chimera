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

import junitparams.*;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


@RunWith(JUnitParamsRunner.class)
public class ClassMapTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    private ClassMap<Object> map;
    
    
    public ClassMapTest() {
        map = new ClassMap<>();
    }
    
    
    @Before
    public void setup() {
        map.map.clear();
    }
    
    
    @Test
    public void getInstance_ThrowsException() {
        exception.expect(ClassCastException.class);
        
        map.map.put(int.class, "");
        
        map.getInstance(int.class);
    }
    
    
    @Test
    @Parameters
    public void getInstanceOrDefault(Object object, int expected) {
        map.map.put(Integer.class, object);
        
        int returned = map.getInstanceOrDefault(Integer.class, 0);
        
        assertEquals(expected, returned);
    }
    
    protected Object[] parametersForGetInstanceOrDefault() {
        return new Object[] {
            new Object[] {5, 5},
            new Object[] {null, 0},
            new Object[] {"", 0}
        };
    }
    
}
