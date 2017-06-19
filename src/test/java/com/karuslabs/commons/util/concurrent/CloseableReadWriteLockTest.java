/*
 * Copyright (C) 2017 Karus Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
