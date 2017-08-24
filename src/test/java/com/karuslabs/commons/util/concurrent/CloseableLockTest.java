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
package com.karuslabs.commons.util.concurrent;

import java.util.function.Supplier;

import junitparams.*;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static com.karuslabs.commons.util.function.CheckedSupplier.wrap;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class CloseableLockTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    
    private CloseableLock lock;
    
    
    public CloseableLockTest() {
        lock = spy(new CloseableLock());
    }
    
    
    @Test
    public void acquire() {
        try (Janitor janitor = lock.acquire()) {
            assertEquals(1, lock.getHoldCount());
        }
        
        assertEquals(0, lock.getHoldCount());
    }
    
    
    @Test
    public void acquireInterruptibly() throws InterruptedException {
        try (Janitor janitor = lock.acquireInterruptibly()) {
            assertEquals(1, lock.getHoldCount());
    }
    
        assertEquals(0, lock.getHoldCount());
    }
    
    
    @Test
    public void acquireInterruptibly_ThrowsException() throws InterruptedException {
        exception.expect(InterruptedException.class);
        
        when(lock.acquireInterruptibly()).thenThrow(InterruptedException.class);
        try (Janitor janitor = lock.acquireInterruptibly()) {
            fail();
        }   
        
        assertEquals(0, lock.getHoldCount());
    }
    
}
