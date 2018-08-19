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
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static com.karuslabs.commons.util.concurrent.ScheduledResultTask.INFINITE;
import static java.lang.Thread.State.WAITING;


@ExtendWith(MockitoExtension.class)
class ScheduledResultTaskTest {
    
    Semaphore semaphore = new Semaphore(0);
    Consumer<Callback<String>> consumer = mock(Consumer.class);
    ScheduledResultTask<String> result = ScheduledResultTask.of(consumer);
    
    
    @Test
    void run() {
        var result = new ScheduledResultTask<>(() -> {}, "result", 1);
        result.run();
        
        assertEquals(0, result.count());
        assertEquals("result", result.await());
    }
    
    
    @Test
    void run_infinite() throws InterruptedException {
        var latch = new CountDownLatch(1);
        var result = new ScheduledResultTask<>(() -> {}, "result");
        var thread = new Thread(() -> {
            try {
                result.run();
                latch.countDown();
                result.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new Error(e);
            }
        });
        
        thread.start();
        latch.await();
        Thread.sleep(1000);
        
        assertEquals(WAITING, thread.getState());
    }
    
    
    @Test
    void exception() {
        var result = ScheduledResultTask.of(new ScheduledCallable<String>() {
            @Override
            protected void run(Callback<String> callback) {
                callback.exception(new RuntimeException());
            }
        });
        result.run();
        
        assertTrue(result.isDone());
    }
    
    
    @Test
    void cancel() {
        result.cancel();
        assertTrue(result.isCancelled());
    }
    
    
    @Test
    void count() {
        assertEquals(INFINITE, result.count());
    }
    
}
