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

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;


public class ClassMapTest {
    
    private ClassMap<Object> map = new ClassMap<>();

    
    @Test
    public void getInstance_ThrowsException() {
        map.put(int.class, "");
        assertThrows(ClassCastException.class, () -> map.getInstance(int.class));
    }
    
    
    @ParameterizedTest
    @MethodSource("getInstanceOrDefault_parameters")
    public <T> void getInstanceOrDefault(Class<T> key, T value, T defaultValue, T expected) {
        map.put(key, value);

        assertEquals(expected, map.getInstanceOrDefault(key, defaultValue));
    }
    
    static Stream<Arguments> getInstanceOrDefault_parameters() {
        return Stream.of(of(Integer.class, 5, 0, 5), of(Integer.class, null, 0, 0), of(Object.class, "value", "", "value"), of(int.class, "", 0, 0));
    }
    
    
    @Test
    public void putInstance() {
        map.putInstance(int.class, 10);
        
        assertEquals(10, map.get(int.class));
    }
    
}
