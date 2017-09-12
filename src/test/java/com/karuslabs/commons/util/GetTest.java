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

import junitparams.*;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;


@RunWith(JUnitParamsRunner.class)
public class GetTest {    
    
    private static final Object A = new Object();
    private static final Object B = new Object();
    private static final Supplier<Object> SUPPLIER = () -> B;
    private static final Supplier<IllegalArgumentException> EXCEPTION = IllegalArgumentException::new;
    
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    
    @Test
    @Parameters
    public void orDefault(Object object, Object value, Object expected) {
        assertEquals(expected, Get.orDefault(object, value));
    }
    
    protected Object[] parametersForOrDefault() {
        return new Object[] {
            new Object[] {A, B, A},
            new Object[] {A, null, A},
            new Object[] {null, B, B}
        };
    }
    
    
    @Test
    @Parameters
    public void orDefault_Supplier(Object object, Supplier<Object> value, Object expected) {
        assertEquals(expected, Get.orDefault(object, value));
    }
    
    protected Object[] parametersForOrDefault_Supplier() {
        return new Object[] {
            new Object[] {A, SUPPLIER, A},
            new Object[] {A, null, A},
            new Object[] {null, SUPPLIER, B}
        };
    }
    
    
    @Test
    @Parameters
    public void orThrow(Object object, Supplier<IllegalArgumentException> value, Object expected) {
        assertEquals(expected, Get.orThrow(object, EXCEPTION));
    }
    
    protected Object[] parametersForOrThrow() {
        return new Object[] {
            new Object[] {A, EXCEPTION, A},
            new Object[] {A, null, A},
        };
    }
    
    
    @Test
    public void orThrow_ThrowsException() {
        exception.expect(IllegalArgumentException.class);
        Get.orThrow(null, EXCEPTION);
    }
    
}
