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

import java.util.concurrent.locks.*;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;


public class CloseableReadWriteLockTest {
    
    private CloseableReadWriteLock lock;
    private ReadWriteLock internalLock;
    private Lock internalReadLock;
    private Lock internalWriteLock;
    
    
    public CloseableReadWriteLockTest() {
        internalReadLock = mock(Lock.class);
        internalWriteLock = mock(Lock.class);
        
        internalLock = when(mock(ReadWriteLock.class).readLock()).thenReturn(internalReadLock).getMock();
        when(internalLock.writeLock()).thenReturn(internalWriteLock);
        
        lock = new CloseableReadWriteLock(internalLock);
    }
    
    
    @Test
    public void closeableReadWriteLock() {
        assertNotNull(new CloseableReadWriteLock());
    }
    
    
    @Test
    public void readLock() {
        assertNotNull(lock.readLock());
        verify(internalReadLock).lock();
    }
    
    
    @Test
    public void janitor_readLock() {
        lock.readLock().close();
        verify(internalReadLock).unlock();
    }
    
    
    @Test
    public void writeLock() {
        assertNotNull(lock.writeLock());
        verify(internalWriteLock).lock();
    }
    
    
    @Test
    public void janitor_writeLock() {
        lock.writeLock().close();
        verify(internalWriteLock).unlock();
    }
    
}
