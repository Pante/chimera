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
package com.karuslabs.commons.util.concurrent.locks;

import com.karuslabs.commons.util.concurrent.locks.Janitor;
import com.karuslabs.commons.util.concurrent.locks.CloseableReadWriteLock;
import java.util.function.Supplier;

import junitparams.*;

import org.junit.*;
import org.junit.runner.RunWith;

import static com.karuslabs.commons.util.function.CheckedSupplier.uncheck;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class CloseableReadWriteLockTest {    
    
    private static CloseableReadWriteLock lock = new CloseableReadWriteLock();
    private CloseableReadWriteLock interrupted;
            
    
    public CloseableReadWriteLockTest() {
        interrupted = spy(new CloseableReadWriteLock());
    }
    
    
    @Test
    @Parameters
    public void acquire(Supplier<Janitor> closeableLock, Supplier<Integer> count) {
        try (Janitor janitor = closeableLock.get()) {
            assertEquals(1, (int) count.get());
        }
        
        assertEquals(0, (int) count.get());
    }
    
    protected Object[] parametersForAcquire() {
        return new Object[] {
            new Supplier[] {lock::acquireReadLock, lock::getReadHoldCount},
            new Supplier[] {uncheck(lock::acquireReadLockInterruptibly), lock::getReadHoldCount},
            new Supplier[] {lock::acquireWriteLock, lock::getWriteHoldCount},
            new Supplier[] {uncheck(lock::acquireWriteLockInterruptibly), lock::getWriteHoldCount}
        };
    }

   
    @Test
    public void acquireReadLockInterruptibly_ThrowsException() throws InterruptedException {
        doThrow(InterruptedException.class).when(interrupted).acquireReadLockInterruptibly();
        
        try (Janitor janitor = interrupted.acquireReadLockInterruptibly()) {
            fail();
            
        } catch (InterruptedException e) {
            
        }
        
        assertEquals(0, interrupted.getReadLockCount());
    }
    
    @Test
    public void acquireWriteLockInterruptibly_ThrowsException() throws InterruptedException {
        doThrow(InterruptedException.class).when(interrupted).acquireWriteLockInterruptibly();
        
        try (Janitor janitor = interrupted.acquireWriteLockInterruptibly()) {
            fail();
            
        } catch (InterruptedException e) {
            
        }
        
        assertEquals(0, interrupted.getWriteHoldCount());
    }
    
}
