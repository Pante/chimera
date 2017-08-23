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

import org.junit.*;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class CloseableReadWriteLockTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    
    private CloseableReadWriteLock lock;
    
    
    public CloseableReadWriteLockTest() {
        lock = spy(new CloseableReadWriteLock());
    }
    
    
    @Test
    public void acquireReadLock() {
        try (Janitor janitor = lock.acquireReadLock()) {
            assertEquals(1, lock.getReadHoldCount());
        }
        
        assertEquals(0, lock.getReadHoldCount());
    }
    
    
    @Test
    public void acquireReadLockInterruptibly() throws InterruptedException {
        try (Janitor janitor = lock.acquireReadLockInterruptibly()) {
            assertEquals(1, lock.getReadHoldCount());
        }
        
        assertEquals(0, lock.getReadHoldCount());
    }

    
    @Test
    public void acquireReadLockInterruptibly_ThrowsException() throws InterruptedException {
        exception.expect(InterruptedException.class);
        
        when(lock.acquireReadLockInterruptibly()).thenThrow(InterruptedException.class);
        try (Janitor janitor = lock.acquireReadLockInterruptibly()) {
            fail();
        }
        
        assertEquals(0, lock.getReadHoldCount());
    }
    
    
    @Test
    public void acquireWriteLock() {
        try (Janitor janitor = lock.acquireWriteLock()) {
            assertEquals(1, lock.getWriteHoldCount());
        }
        
        assertEquals(0, lock.getWriteHoldCount());
    }
    
    
    @Test
    public void acquireWriteLockInterruptibly() throws InterruptedException {
        try (Janitor janitor = lock.acquireWriteLockInterruptibly()) {
            assertEquals(1, lock.getWriteHoldCount());
        }
        
        assertEquals(0, lock.getWriteHoldCount());
    }
    
    
    @Test
    public void acquireWriteLockInterruptibly_ThrowsException() throws InterruptedException {
        exception.expect(InterruptedException.class);
        
        when(lock.acquireWriteLockInterruptibly()).thenThrow(InterruptedException.class);
        try (Janitor janitor = lock.acquireWriteLockInterruptibly()) {
            fail();
        }
        
        assertEquals(0, lock.getWriteHoldCount());
    }
    
}
