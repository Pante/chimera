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
import org.junit.jupiter.params.provider.MethodSource;

import static com.karuslabs.commons.util.function.CheckedSupplier.uncheck;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.*;


@TestInstance(PER_CLASS)
public class CloseableLockTest {    
    
    private static CloseableLock lock = new CloseableLock();
    
    
    @ParameterizedTest
    @MethodSource("acquire_parameters")
    public void acquire(Supplier<Janitor> closeableLock) {
        try (Janitor janitor = closeableLock.get()) {
            assertEquals(1, lock.getHoldCount());
        }
        
        assertEquals(0, lock.getHoldCount());
    }
    
    static Stream<Supplier> acquire_parameters() {
        return Stream.of(lock::acquire, uncheck(lock::acquireInterruptibly));
    }
    
    
    @Test
    public void acquireInterruptibly() throws InterruptedException {
        CloseableLock aLock = spy(new CloseableLock());
        doThrow(InterruptedException.class).when(aLock).acquireInterruptibly();
        
        try (Janitor janitor = aLock.acquireInterruptibly()) {
            fail("lock was still acquired");
            
        } catch (InterruptedException e) {
            
        }
        
        assertEquals(0, lock.getHoldCount());
    }
    
}
