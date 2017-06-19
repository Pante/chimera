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

import java.util.concurrent.locks.Lock;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;


public class CloseableLockTest {
    
    private CloseableLock lock;
    private Lock internal;
    
    
    public CloseableLockTest() {
        internal = mock(Lock.class);
        lock = new CloseableLock(internal);
    }
    
    
    @Test
    public void closeableLock() {
        assertNotNull(new CloseableLock().getLock());
    }
    
    
    @Test
    public void lock() {
        assertNotNull(lock.lock());
        verify(internal).lock();
    }
    
    
    @Test
    public void janitor_Unlock() {
        lock.lock().close();
        
        verify(internal).unlock();
    }
    
}
