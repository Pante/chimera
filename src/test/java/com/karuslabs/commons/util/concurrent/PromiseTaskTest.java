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

import static com.karuslabs.commons.util.concurrent.PromiseTask.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


public class PromiseTaskTest {
    
    private Runnable runnable = mock(Runnable.class);
    private PromiseTask<String> task = spy(PromiseTask.of(runnable, "result"));
    
    
    @Test
    public void run() {
        doNothing().when(task).done();
        
        task.run();
        
        assertNull(task.thrown);
        verify(runnable).run();
        verify(task).done();
    }
    
    
    @Test
    public void run_ThrowsException() throws Exception {
        doNothing().when(task).done();
        doThrow(Exception.class).when(runnable).run();
        
        task.run();
        
        assertEquals(Exception.class, task.thrown.getClass());
        verify(task).process();
        verify(task).done();
    }
    
    
    @Test
    public void done() {
        doNothing().when(task).finish(COMPLETED);
        
        task.done();
        
        verify(task).finish(COMPLETED);
    }
    
    
    @Test
    public void cancel() {
        doReturn(true).when(task).cancel(true);
        
        task.cancel();
        
        verify(task).cancel(true);
    }
    
    
    @ParameterizedTest
    @CsvSource({"1, true, 1, true", "2, true, 0, false", "1, false, 0, false"})
    public void cancel_interrupt(int state, boolean interrupt, int times, boolean expected) {
        doNothing().when(task).finish(CANCELLED);
        
        task.state = state;
        task.interrupted = false;
        
        boolean success = task.cancel(interrupt);
        
        verify(task, times(times)).finish(CANCELLED);
        assertEquals(expected, success);
        assertEquals(expected, task.interrupted);
    }
    
    
    @Test
    public void finish() {
        doNothing().when(task).cancelThis();
        assertEquals(NEW, task.state);
        assertEquals(1, task.latch.getCount());
        
        task.finish(COMPLETED);
        
        verify(task).cancelThis();
        assertEquals(COMPLETED, task.state);
        assertEquals(0, task.latch.getCount());
    }
    
    
    @Test
    public void get() throws InterruptedException, ExecutionException {
        task.latch = mock(CountDownLatch.class);
        
        assertEquals("result", task.get());
        verify(task.latch).await();
    }
    
    
    @Test
    public void get_Time() throws InterruptedException, ExecutionException, TimeoutException {
        task.latch = mock(CountDownLatch.class);
        
        assertEquals("result", task.get(0, TimeUnit.DAYS));
        verify(task.latch).await(0, TimeUnit.DAYS);
    }
    
    
    @Test
    public void handle() throws ExecutionException {
        task.state = COMPLETED;
        
        assertEquals("result", task.handle());
    }
    
    
    @ParameterizedTest
    @MethodSource("handle_parameters")
    public void handle_ThrowsException(Class<? extends Exception> type, Exception thrown, int state) {
        task.thrown = thrown;
        task.state = state;
        
        assertThrows(type, task::handle);
    }
    
    static Stream<Arguments> handle_parameters() {
        return Stream.of(of(ExecutionException.class, new Exception(), COMPLETED), of(CancellationException.class, null, CANCELLED));
    }
    
    
    @ParameterizedTest
    @CsvSource({"3, true", "1, false"})
    public void isCancelled(int state, boolean expected) {
        task.state = state;
        assertEquals(expected, task.isCancelled());
    }
    
    
    @ParameterizedTest
    @CsvSource({"1, false", "2, true", "3, true"})
    public void isDone(int state, boolean expected) {
        task.state = state;
        assertEquals(expected, task.isDone());
    }
    
    
    @Test
    public void of_Runnable() {
        Runnable runnable = mock(Runnable.class);
        PromiseTask<String> task = PromiseTask.of(runnable, "result");
        
        task.run();
        verify(runnable).run();
        assertEquals("result", task.await());
    }
    
    
    @Test
    public void of_Callable() {
        Callable<String> callable = () -> "result";
        PromiseTask<String> task = PromiseTask.of(callable);
        
        task.run();
        assertEquals("result", task.await());
    }
    
}
