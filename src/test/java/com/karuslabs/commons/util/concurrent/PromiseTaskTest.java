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

import junitparams.*;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class PromiseTaskTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    private PromiseTask<String> task;
    private PromiseTask<String> stub;
    
    
    public PromiseTaskTest() {
        task = spy(new BrokenPromiseTask(1));
        stub = spy(new BrokenPromiseTask(mock(CountDownLatch.class), 0));
    }
    
    
    @Before
    public void reset() {
        if (task.latch.getCount() == 0) {
            task = spy(new BrokenPromiseTask(1));
        }
    }
    
    
    @Test
    @Parameters({"1, false, 1, 0", "0, false, 0, 1", "1, true, 0, 1"})
    public void run(int iterations, boolean done, int process, int end) {
        task.total = iterations;
        task.current = 0;
        task.done.set(done);
        
        doNothing().when(task).end();
        doNothing().when(task).callback();
        
        task.run();
        
        verify(task, times(process)).process();
        assertTrue(iterations == 1 && !done ? task.getCurrent() == 1 : task.getCurrent() == 0);
        
        verify(task, times(end)).end();
        verify(task, times(end)).callback();
    }
    
    
    @Test
    public void end() {
        doNothing().when(task).cancelThis();
        task.done.set(false);
        
        task.end();
        
        verify(task).cancelThis();
        assertTrue(task.isDone());
        assertEquals(0, task.latch.getCount());
    }
    
    
    @Test
    public void cancel() {
        doReturn(true).when(task).cancel(anyBoolean());
        
        task.cancel();
        
        verify(task).cancel(true);
    }
    
    
    @Test
    @Parameters({"false, true, true, 1", "false, false, false, 0", "true, true, false, 0"})
    public void cancel_Interrupt(boolean done, boolean interrupt, boolean expected, int end) {
        task.done.set(done);
        task.cancelled.set(false);
        doNothing().when(task).end();
        
        assertEquals(expected, task.cancel(interrupt));
        assertEquals(expected, task.isCancelled());
        verify(task, times(end)).end();
    }
    
        
    @Test
    public void get() throws InterruptedException, ExecutionException {
        doNothing().when(stub.latch).await();
        
        stub.get();
        
        verify(stub.latch).await();
    }
    
    
    @Test
    public void get_Time() throws InterruptedException, ExecutionException, TimeoutException {
        doReturn(true).when(stub.latch).await(0, TimeUnit.DAYS);
        
        stub.get(0, TimeUnit.DAYS);
        
        verify(stub.latch).await(0, TimeUnit.DAYS);
    }
    
    
    @Test
    public void get_Time_ThrowsException() throws InterruptedException, ExecutionException, TimeoutException {
        exception.expect(TimeoutException.class);
        
        doReturn(false).when(stub.latch).await(0, TimeUnit.DAYS);
        
        stub.get(0, TimeUnit.DAYS);
    }
    
    
    
    private static class BrokenPromiseTask extends PromiseTask<String> { 
        
        public BrokenPromiseTask(long iterations) {
            super(iterations);
        }
        
        public BrokenPromiseTask(CountDownLatch latch, long iterations) {
            super(latch, iterations);
        }
        

        @Override
        protected void process() {
            
        }

        @Override
        protected String value() {
            return "value";
        }
        
    }
    
}
