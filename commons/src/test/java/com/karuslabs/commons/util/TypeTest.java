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

package com.karuslabs.commons.util;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class TypeTest {
    
    @ParameterizedTest
    @MethodSource({"types"})
    void of(Object object) {
        assertEquals(Type.of(object.getClass()).boxed, object.getClass());
    }
    
    static Stream<Object> types() {
        return Stream.of(true, 'a', "b", (byte) 0, (short) 0, 0, 0L, (float) 0, (double) 0);
    }
    
    
    @Test
    void of_type() {
        var type = Type.of(new Object().getClass());
        
        assertEquals(Object.class, type.boxed);
        assertEquals(Object.class, type.unboxed);
    }
    
    
    @Test
    void of_null() {
        assertEquals(Void.class, Type.of(Void.class).boxed);
        assertEquals(null, Type.of(Void.class).unboxed);
        
        assertEquals(Void.class, Type.of(null).boxed);
        assertEquals(null, Type.of(null).unboxed);
    }
    
}
