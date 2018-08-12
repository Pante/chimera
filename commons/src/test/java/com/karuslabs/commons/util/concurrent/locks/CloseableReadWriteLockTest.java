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

package com.karuslabs.commons.util.concurrent.locks;

import com.karuslabs.commons.util.concurrent.locks.CloseableReadWriteLock.*;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.concurrent.TimeUnit.DAYS;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CloseableReadWriteLockTest {
    
    CloseableReadWriteLock lock = new CloseableReadWriteLock();
    CloseableReadLock reader = lock.readLock();
    CloseableWriteLock writer = lock.writeLock();
    
    
    CloseableReadWriteLockTest() {
        reader.lock = mock(ReadLock.class);
        writer.lock = mock(WriteLock.class);
    }
    
    
    @ParameterizedTest
    @MethodSource({"lock_provider"})
    void lock(Lock lock, Lock internal) throws InterruptedException {
        lock.lock();
        verify(internal).lock();
        
        lock.lockInterruptibly();
        verify(internal).lockInterruptibly();
        
        lock.newCondition();
        verify(internal).newCondition();

        lock.tryLock();
        verify(internal).tryLock();
        
        lock.tryLock(1, DAYS);
        verify(internal).tryLock(1, DAYS);
        
        lock.unlock();
        verify(internal).unlock();
        
        assertEquals(lock.toString(), "delegate");
    }

    
    @ParameterizedTest
    @MethodSource({"lock_provider"})
    void acquisition(Acquirable lock, Lock internal) throws InterruptedException {
        lock.acquire();
        verify(internal).lock();
        
        lock.acquireInterruptibly();
        verify(internal).lockInterruptibly();
        
        ((Acquisition) lock).close();
        verify(internal).unlock();
    }
    
    
    static Stream<Arguments> lock_provider() {
        var lock = new CloseableReadWriteLock();
        var reader = lock.readLock();
        var writer = lock.writeLock();
        
        reader.lock = when(mock(ReadLock.class).toString()).thenReturn("delegate").getMock();
        writer.lock = when(mock(WriteLock.class).toString()).thenReturn("delegate").getMock();

        return Stream.of(of(reader, reader.lock), of(writer, writer.lock));
    }
    
    
    @Test
    void closeableWriteLock() {
        writer.getHoldCount();
        verify(writer.lock).getHoldCount();
        
        writer.isHeldByCurrentThread();
        verify(writer.lock).isHeldByCurrentThread();
    }
    
}
