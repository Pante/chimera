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
package com.karuslabs.commons.util.concurrent;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class MaybeTest {
    
    static final String expected = "expected";
    static final Maybe<String> maybe = new Maybe<>(() -> expected);
    static final Maybe<String> exceptional = new Maybe<>(() -> { throw new IllegalArgumentException(); }, null);
    
    
    @BeforeAll
    static void before() {
        maybe.run();
        exceptional.run();
    }
    
    
    @Test
    void maybe() {
        assertEquals(expected, maybe.maybe().orElseThrow());
    }
    
    
    @Test
    void maybe_throws_exception() {
        assertEquals(Optional.empty(), exceptional.maybe());
    }
    
    
    @Test
    void maybe_timeout() {
        assertEquals(expected, maybe.maybe(1, TimeUnit.MINUTES).orElseThrow());
    }
    
    
    @Test
    void maybe_timeout_throws_exception() {
        assertEquals(Optional.empty(), exceptional.maybe(0, TimeUnit.MINUTES));
    }
    
    
    @Test
    void value() {
        assertEquals(expected, maybe.value());
    }
    
    
    @Test
    void value_throws_exception() {
        assertNull(exceptional.value());
    }
    
    
    @Test
    void value_timeout() {
        assertEquals(expected, maybe.value(1, TimeUnit.MINUTES));
    }
    
    
    @Test
    void value_timeout_throws_exception() {
        assertNull(exceptional.value(0, TimeUnit.MINUTES));
    }
    
}
