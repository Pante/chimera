/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
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
package com.karuslabs.commons.util.collection;

import java.util.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;


class ClassMapTest {    
    
    @ParameterizedTest
    @MethodSource("parameters")
    void containsKey(ClassMap<Object> map) {
        map.put(int.class, 1);
        
        assertTrue(map.containsKey(int.class));
        assertFalse(map.containsKey(Integer.class));
    }
    
    
    @ParameterizedTest
    @MethodSource("parameters")
    void containsValue(ClassMap<Object> map) {  
        map.put(int.class, 1); 
        assertTrue(map.containsValue(1));
    }
    
    
    @ParameterizedTest
    @MethodSource("parameters")
    void get(ClassMap<Object> map) {
        map.put(String.class, "test");
        assertEquals(String.class, map.get(String.class).getClass());
    }
    
    
    @ParameterizedTest
    @MethodSource("parameters")
    void getOrDefault_value(ClassMap<Object> map) {
        map.put(int.class, 1);
        assertEquals(1, (int) map.getOrDefault(int.class, 2));
    }
    
    
    @ParameterizedTest
    @MethodSource("parameters")
    void getOrDefault_default(ClassMap<Object> map) {
        map.map().put(int.class, "invalid");
        assertEquals(2, (int) map.getOrDefault(int.class, 2));
    }
    
    
    @ParameterizedTest
    @MethodSource("parameters")
    void put(ClassMap<Object> map) {
        map.put(String.class, "first");
        assertEquals("first", map.put(String.class, "second"));
    }
    
    
    @ParameterizedTest
    @MethodSource("parameters")
    void remove(ClassMap<Object> map) {
        map.put(int.class, 1); 
        map.remove(int.class);
        assertFalse(map.containsKey(int.class));
    }
    
    
    static Stream<ClassMap<Object>> parameters() {
        var hashed = ClassMap.of();
        var proxied = ClassMap.of(new HashMap<>());
        
        return Stream.of(hashed, proxied);
    }
    
}


class HashClassMapTest {
    
    ClassMap<Object> map = ClassMap.of(1);
    
    
    @Test
    void map() {
        assertSame(map, map.map());
    }
    
}


class ProxiedClassMapTest {
    
    Map<Class<? extends Object>, Object> proxied = new HashMap<>(0);
    ClassMap<Object> map = ClassMap.of(proxied);
    
    
    @Test
    void map() {
        assertSame(proxied, map.map());
    }
    
}