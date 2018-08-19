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

package com.karuslabs.commons.util.concurrent;

import java.util.concurrent.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.concurrent.TimeUnit.DAYS;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ResultTaskTest {
    
    @Spy ResultTask<String> result = new ResultTask<>(() -> "value");
    
    
    @Test
    void await() {
        result.run();
        assertEquals("value", result.await());
    }
    
    
    @Test
    void await_timed() {
        var result = new ResultTask<>(() -> {}, "value");
        result.run();
        assertEquals("value", result.await(1, DAYS));
    }
    
    
    @ParameterizedTest
    @MethodSource({"await_throws_exception_provider"})
    void await_throws_exception(Class<? extends Exception> type, Class<? extends Exception> expected) throws InterruptedException, ExecutionException {
        doThrow(type).when(result).get();
        
        assertEquals(type, assertThrows(expected, result::await).getCause().getClass());
    }
    
    
    static Stream<Arguments> await_throws_exception_provider() {
        return Stream.of(
            of(InterruptedException.class, UncheckedInterruptedException.class),
            of(ExecutionException.class, UncheckedExecutionException.class)
        );
    }
    
    
    @ParameterizedTest
    @MethodSource({"await_throws_exception_provider", "await_timed_throws_exception_provider"})
    void await_timed_throws_exception(Class<? extends Exception> type, Class<? extends Exception> expected) throws InterruptedException, ExecutionException, TimeoutException {
        doThrow(type).when(result).get(0, DAYS);
        
        assertEquals(type, assertThrows(expected, () -> result.await(0, DAYS)).getCause().getClass());
    }
    
    static Stream<Arguments> await_timed_throws_exception_provider() {
        return Stream.of(of(TimeoutException.class, UncheckedTimeoutException.class));
    }
    
}
