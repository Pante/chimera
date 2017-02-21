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

import junitparams.*;

import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;


@RunWith(JUnitParamsRunner.class)
public class ClassMapTest {
    
    private static Class<ArrayList> key = ArrayList.class;
    private static ArrayList value = new ArrayList<>(0);
    
    private ClassMap<List> map;
    
    
    public ClassMapTest() {
        map = new ClassMap<>();
    }
    
    
    @Before
    public void setup() {
        map.clear();
    }
    
    
    @Test
    @Parameters
    public void getCasted(ArrayList value) {
        map.put(key, value);
        ArrayList returned = map.getCasted(key);
        
        assertEquals(value, returned);
    }
    
    protected Object[] parametersForGetCasted() {
        return new Object[] {
            value, null
        };
    }
    
    
    @Test
    @Parameters
    public void getCastedOrDefault(ArrayList value, ArrayList defaultValue, ArrayList expected) {
        map.put(key, value);
        ArrayList returned = map.getCastedOrDefault(key, defaultValue);
        
        assertEquals(expected, returned);
    }
    
    protected Object[] parametersForGetCastedOrDefault() {
        return new Object[] {
            new Object[] {value, null, value},
            new Object[] {null, value, value}
        };
    }
    
    
    @Test
    public void get() {
        map.put(key, value);
        List returned = map.get(key);
        
        assertEquals(value, returned);
    }
    
}
