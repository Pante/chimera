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
    
    
    @Test
    public void putInstance() {
        map.putInstance(int.class, 10);
        
        assertEquals(10, map.get(int.class));
    }
    
}
