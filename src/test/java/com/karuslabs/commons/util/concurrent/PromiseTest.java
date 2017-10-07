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

import java.util.concurrent.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static java.util.concurrent.TimeUnit.DAYS;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


public class PromiseTest {
    
    private Future<String> future = mock(Future.class);
    private Promise<String> promise = spy(Promise.of(future));
    
    
    @Test
    public void await() {
        doThrow(CancellationException.class).when(promise).obtain();
        assertNull(promise.await());
    }
    
    
    @Test
    public void await_Time() {
        doThrow(CancellationException.class).when(promise).obtain(anyLong(), any());
        assertNull(promise.await(0, null));
    }
    
    
    @ParameterizedTest
    @MethodSource("obtain_parameters")
    public void obtain(Class<? extends Exception> type, Exception thrown) throws InterruptedException, ExecutionException {
        doThrow(thrown).when(promise).get();
        
        assertEquals(thrown, assertThrows(type, () -> promise.obtain()).getCause());
    }
    
    static Stream<Arguments> obtain_parameters() {
        return Stream.of(
            of(UncheckedExecutionException.class, new ExecutionException(null)), 
            of(UncheckedInterruptedException.class, new InterruptedException())
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("obtain_Time_parameters")
    public void obtain_Time(Class<? extends Exception> type, Exception thrown) throws InterruptedException, ExecutionException, TimeoutException {
        doThrow(thrown).when(promise).get(anyLong(), any());
        
        assertEquals(thrown, assertThrows(type, () -> promise.obtain(0, DAYS)).getCause());
    }
    
    static Stream<Arguments> obtain_Time_parameters() {
        return Stream.of(
            of(UncheckedExecutionException.class, new ExecutionException(null)), 
            of(UncheckedInterruptedException.class, new InterruptedException()),
            of(UncheckedTimeoutException.class, new TimeoutException())
        );
    }
    
}
