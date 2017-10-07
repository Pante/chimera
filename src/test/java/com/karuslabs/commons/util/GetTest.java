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
package com.karuslabs.commons.util;

import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.junit.jupiter.params.provider.Arguments.of;


@TestInstance(PER_CLASS)
public class GetTest {    
    
    private static Object a = new Object();
    private static Object b = new Object();
    private static Supplier<Object> supplier = () -> b;
    private static Supplier<IllegalArgumentException> exception = IllegalArgumentException::new;
    
    
    @ParameterizedTest
    @MethodSource("orDefault_parameters")
    public void orDefault(Object object, Object value, Object expected) {
        assertEquals(expected, Get.orDefault(object, value));
    }
    
    static Stream<Arguments> orDefault_parameters() {
        return Stream.of(of(a, b, a), of(a, null, a), of(null, b, b));
    }
    
    
    @ParameterizedTest
    @MethodSource("orDefault_Supplier_parameters")
    public void orDefault_Supplier(Object object, Supplier<Object> value, Object expected) {
        assertEquals(expected, Get.orDefault(object, value));
    }
    
    static Stream<Arguments> orDefault_Supplier_parameters() {
        return Stream.of(of(a, supplier, a), of(a, null, a), of(null, supplier, b));
    }
    
    
    @ParameterizedTest
    @MethodSource("orThrow_parameters")
    public void orThrow(Object object, Supplier<IllegalArgumentException> value, Object expected) {
        assertEquals(expected, Get.orThrow(object, exception));
    }
    
    static Stream<Arguments> orThrow_parameters() {
        return Stream.of(of(a, exception, a), of(a, null, a));
    }
    
    
    @Test
    public void orThrow_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Get.orThrow(null, exception));
    }
    
}
