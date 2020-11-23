/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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
package com.karuslabs.puff.mock;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class MockNameTest {

    public static final String VALUE = Object.class.getSimpleName();
    public static final MockName NAME = new MockName(Object.class);
    
    @Test
    void contentEquals() {
        assertTrue(NAME.contentEquals(VALUE));
    }
    
    @Test
    void length() {
        assertEquals(VALUE.length(), NAME.length());
    }
    
    @Test
    void charAt() {
        assertEquals(VALUE.charAt(1), NAME.charAt(1));
    }
    
    @Test
    void subSequence() {
        assertEquals(VALUE.subSequence(0, 2), NAME.subSequence(0, 2));
    }
    
    @Test
    void toString_() {
        assertEquals(VALUE, NAME.toString());
    }
    
}
