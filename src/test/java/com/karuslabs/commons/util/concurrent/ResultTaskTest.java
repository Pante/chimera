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

import static com.karuslabs.commons.util.concurrent.ResultTask.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


class ResultTaskTest {
    
    Runnable runnable = mock(Runnable.class);
    ResultTask<String> result = spy(ResultTask.of(runnable, "result"));
    
    
    @Test
    void run() {
        doNothing().when(result).done();
        
        result.run();
        
        assertNull(result.thrown);
        verify(runnable).run();
        verify(result).done();
    }
    
    
    @Test
    void run_ThrowsException() throws Exception {
        doNothing().when(result).done();
        doThrow(Exception.class).when(runnable).run();
        
        result.run();
        
        assertEquals(Exception.class, result.thrown.getClass());
        verify(result).process();
        verify(result).done();
    }
    
    
    @Test
    void done() {
        doNothing().when(result).finish(COMPLETED);
        
        result.done();
        
        verify(result).finish(COMPLETED);
    }
    
    
    @Test
    void cancel() {
        doReturn(true).when(result).cancel(true);
        
        result.cancel();
        
        verify(result).cancel(true);
    }
    
    
    @ParameterizedTest
    @CsvSource({"1, true, 1, true", "2, true, 0, false", "1, false, 0, false"})
    void cancel_interrupt(int state, boolean interrupt, int times, boolean expected) {
        doNothing().when(result).finish(CANCELLED);
        
        result.state = state;
        result.interrupted = false;
        
        boolean success = result.cancel(interrupt);
        
        verify(result, times(times)).finish(CANCELLED);
        assertEquals(expected, success);
        assertEquals(expected, result.interrupted);
    }
    
    
    @Test
    void finish() {
        doNothing().when(result).cancelThis();
        assertEquals(NEW, result.state);
        assertEquals(1, result.latch.getCount());
        
        result.finish(COMPLETED);
        
        verify(result).cancelThis();
        assertEquals(COMPLETED, result.state);
        assertEquals(0, result.latch.getCount());
    }
    
    
    @Test
    void get() throws InterruptedException, ExecutionException {
        result.latch = mock(CountDownLatch.class);
        
        assertEquals("result", result.get());
        verify(result.latch).await();
    }
    
    
    @Test
    void get_Time() throws InterruptedException, ExecutionException, TimeoutException {
        result.latch = mock(CountDownLatch.class);
        
        assertEquals("result", result.get(0, TimeUnit.DAYS));
        verify(result.latch).await(0, TimeUnit.DAYS);
    }
    
    
    @Test
    void handle() throws ExecutionException {
        result.state = COMPLETED;
        
        assertEquals("result", result.handle());
    }
    
    
    @ParameterizedTest
    @MethodSource("handle_parameters")
    void handle_ThrowsException(Class<? extends Exception> type, Exception thrown, int state) {
        result.thrown = thrown;
        result.state = state;
        
        assertThrows(type, result::handle);
    }
    
    static Stream<Arguments> handle_parameters() {
        return Stream.of(of(ExecutionException.class, new Exception(), COMPLETED), of(CancellationException.class, null, CANCELLED));
    }
    
    
    @ParameterizedTest
    @CsvSource({"3, true", "1, false"})
    void isCancelled(int state, boolean expected) {
        result.state = state;
        assertEquals(expected, result.isCancelled());
    }
    
    
    @ParameterizedTest
    @CsvSource({"1, false", "2, true", "3, true"})
    public void isDone(int state, boolean expected) {
        result.state = state;
        assertEquals(expected, result.isDone());
    }
    
    
    @Test
    void of_Runnable() {
        Runnable runnable = mock(Runnable.class);
        ResultTask<String> task = ResultTask.of(runnable, "result");
        
        task.run();
        verify(runnable).run();
        assertEquals("result", task.await());
    }
    
    
    @Test
    void of_Callable() {
        Callable<String> callable = () -> "result";
        ResultTask<String> task = ResultTask.of(callable);
        
        task.run();
        assertEquals("result", task.await());
    }
    
}
