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
class EventualTaskTest {
    
    static final String expected = "expected";
    static final EventualTask<String> eventual = new EventualTask<>(() -> expected);
    static final EventualTask<String> exceptional = new EventualTask<>(() -> { throw new IllegalArgumentException(); }, null);
    
    
    @BeforeAll
    static void before() {
        eventual.run();
        exceptional.run();
    }
    
    
    @Test
    void acquire() {
        assertEquals(expected, eventual.acquire().orElseThrow());
    }
    
    
    @Test
    void acquire_throws_exception() {
        assertEquals(Optional.empty(), exceptional.acquire());
    }
    
    
    @Test
    void acquire_timeout() {
        assertEquals(expected, eventual.acquire(1, TimeUnit.MINUTES).orElseThrow());
    }
    
    
    @Test
    void acquire_timeout_throws_exception() {
        assertEquals(Optional.empty(), exceptional.acquire(0, TimeUnit.MINUTES));
    }
    
    
    @Test
    void await() {
        assertEquals(expected, eventual.await());
    }
    
    
    @Test
    void await_throws_exception() {
        assertNull(exceptional.await());
    }
    
    
    @Test
    void await_timeout() {
        assertEquals(expected, eventual.await(1, TimeUnit.MINUTES));
    }
    
    
    @Test
    void await_timeout_throws_exception() {
        assertNull(exceptional.await(0, TimeUnit.MINUTES));
    }
    
}
