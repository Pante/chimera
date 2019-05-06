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

import java.util.concurrent.Future;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class RunnableRepetitionTest {
    
    Runnable runnable = mock(Runnable.class);
    Repetition<String> continual = new RunnableRepetition<>(runnable, 0);
    Future<String> context = mock(Future.class);
    
    
    @BeforeEach
    void before() {
        continual.set(context);
    }
    
    
    @Test
    void run_timed() {
        var continual = new RunnableRepetition<String>(runnable, 1);
        continual.set(context);
        
        continual.run();
        verify(runnable).run();
        
        continual.run();
        verify(context).cancel(false);
    }
    
    
    @Test
    void run_finish() {
        continual.run();
        verify(runnable, times(0)).run();
        verify(context).cancel(false);
    }
    
    
    @Test
    void set_throws_exception() {
        assertEquals("Context has already been set", assertThrows(IllegalStateException.class, () -> continual.set(context)).getMessage());
    }
    
}
