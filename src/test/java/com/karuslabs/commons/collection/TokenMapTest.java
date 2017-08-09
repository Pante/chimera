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

import com.karuslabs.commons.collection.TokenMap.Key;

import junitparams.*;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static com.karuslabs.commons.collection.TokenMap.key;
import static org.junit.Assert.*;


@RunWith(JUnitParamsRunner.class)
public class TokenMapTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    private TokenMap<Object> map;
    private Key<Integer> key;
    
    
    public TokenMapTest() {
        map = new TokenMap<>();
        key = new TokenMap.Key<>("", Integer.class);
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
    @Parameters(method = "keys")
    public void equals(Key<?> aKey, boolean isEqual) {
        assertEquals(isEqual, key.equals(aKey));
    }
    
    @Test
    @Parameters(method= "keys")
    public void hashCode(Key<?> aKey, boolean isEqual) {
        assertEquals(isEqual, key.hashCode() == aKey.hashCode());
    }
    
    protected Object[] keys() {
        return new Object[] {
            new Object[] {key("", Integer.class), true},
            new Object[] {key("", String.class), false},
            new Object[] {key("wrong", Integer.class), false},
            new Object[] {key("wrong", String.class), false}
        };
}
    
}
