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

import com.karuslabs.commons.util.concurrent.locks.AutoReadWriteLock.*;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.junit.jupiter.*;
import org.mockito.quality.Strictness;

import static java.util.concurrent.TimeUnit.DAYS;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AutoReadWriteLockTest {
    
    @ParameterizedTest
    @MethodSource({"lock_parameters"})
    void lock_delegate(Lock lock, Lock delegate) throws InterruptedException {
        lock.lock();
        verify(delegate).lock();
        
        lock.lockInterruptibly();
        verify(delegate).lockInterruptibly();
        
        lock.newCondition();
        verify(delegate).newCondition();

        lock.tryLock();
        verify(delegate).tryLock();
        
        lock.tryLock(1, DAYS);
        verify(delegate).tryLock(1, DAYS);
        
        lock.unlock();
        verify(delegate).unlock();
        
        assertEquals(lock.toString(), "delegate");
    }

    
    @ParameterizedTest
    @MethodSource({"lock_parameters"})
    void acquire_delegate_unlocks(Acquirable lock, Lock delegate) throws InterruptedException {
        try (var mutex = lock.acquire()) {
            verify(delegate).lock();
            
        } finally {
            verify(delegate).unlock();
        }
    }
    
    @ParameterizedTest
    @MethodSource({"lock_parameters"})
    void acquireInterruptibly_delegate_unlocks(Acquirable lock, Lock delegate) throws InterruptedException {
        try (var mutex = lock.acquireInterruptibly()) {
            verify(delegate).lockInterruptibly();
            
        } finally {
            verify(delegate).unlock();
        }
    } 
    
    
    static Stream<Arguments> lock_parameters() {
        ReadLock reader = when(mock(ReadLock.class).toString()).thenReturn("delegate").getMock();
        var autoreader = new AutoReadLock(new AutoReadWriteLock(), reader);
        
        WriteLock writer = when(mock(WriteLock.class).toString()).thenReturn("delegate").getMock();
        var autowriter = new AutoWriteLock(new AutoReadWriteLock(), writer);

        return Stream.of(of(autoreader, reader), of(autowriter, writer));
    }
    
    
    @Test
    void writelock_delegates() {
        WriteLock writer = when(mock(WriteLock.class).toString()).thenReturn("delegate").getMock();
        var autowriter = new AutoWriteLock(new AutoReadWriteLock(), writer);
        
        autowriter.getHoldCount();
        verify(writer).getHoldCount();
        
        autowriter.isHeldByCurrentThread();
        verify(writer).isHeldByCurrentThread();
    }
    
}
