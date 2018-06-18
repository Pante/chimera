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

import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static com.karuslabs.commons.util.function.CheckedSupplier.uncheck;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


@TestInstance(PER_CLASS)
class CloseableReadWriteLockTest {    
    
    static CloseableReadWriteLock lock = new CloseableReadWriteLock();
    
    CloseableReadWriteLock interrupted = spy(new CloseableReadWriteLock());

    
    @ParameterizedTest
    @MethodSource("acquire_parameters")
    void acquire(Supplier<Janitor> closeableLock, Supplier<Integer> count) {
        try (Janitor janitor = closeableLock.get()) {
            assertEquals(1, (int) count.get());
        }
        
        assertEquals(0, (int) count.get());
    }
    
    static Stream<Arguments> acquire_parameters() {
        return Stream.of(
            of(wrap(lock::acquireReadLock), wrap(lock::getReadHoldCount)),
            of(wrap(uncheck(lock::acquireReadLockInterruptibly)), wrap(lock::getReadHoldCount)),
            of(wrap(lock::acquireWriteLock), wrap(lock::getWriteHoldCount)),
            of(wrap(uncheck(lock::acquireWriteLockInterruptibly)), wrap(lock::getWriteHoldCount))
        );
    }
    
    static Object wrap(Supplier<?> supplier) {
        return supplier;
    }

   
    @Test
    void acquireReadLockInterruptibly() throws InterruptedException {
        doThrow(InterruptedException.class).when(interrupted).acquireReadLockInterruptibly();
        
        try (Janitor janitor = interrupted.acquireReadLockInterruptibly()) {
            fail("Still acquired readlock");
            
        } catch (InterruptedException e) {
            
        }
        
        assertEquals(0, interrupted.getReadLockCount());
    }
    
    
    @Test
    void acquireWriteLockInterruptibly() throws InterruptedException {
        doThrow(InterruptedException.class).when(interrupted).acquireWriteLockInterruptibly();
        
        try (Janitor janitor = interrupted.acquireWriteLockInterruptibly()) {
            fail("still acquired writelock");
            
        } catch (InterruptedException e) {
            
        }
        
        assertEquals(0, interrupted.getWriteHoldCount());
    }
    
}
