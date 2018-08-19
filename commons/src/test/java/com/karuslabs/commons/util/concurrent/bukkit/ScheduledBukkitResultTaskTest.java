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

package com.karuslabs.commons.util.concurrent.bukkit;

import com.karuslabs.commons.util.concurrent.*;

import java.util.concurrent.*;
import java.util.function.Consumer;

import org.bukkit.scheduler.BukkitTask;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static java.lang.Thread.State.WAITING;


@ExtendWith(MockitoExtension.class)
class ScheduledBukkitResultTaskTest {
    
    BukkitTask task = mock(BukkitTask.class);
    Consumer<Callback<String>> consumer = mock(Consumer.class);
    ScheduledBukkitResultTask<String> result = ScheduledBukkitResultTask.of(consumer);
    
    
    @Test
    void run() throws InterruptedException, ExecutionException {
        var result = new ScheduledBukkitResultTask<>(() -> {}, "value", 1);
        var thread = new Thread(result);
        
        thread.start();
        Thread.sleep(1000);
        
        assertEquals(WAITING, thread.getState());
        
        result.setTask(task);
        
        assertEquals("value", result.get());
    }
    
    
    @Test
    void run_throws_exception() throws InterruptedException {
        var result = ScheduledBukkitResultTask.of(new ScheduledCallable<String>() {
            @Override
            protected void run(Callback<String> callback) {
                
            }
        });
        
        result.synchronizer = mock(LatchSynchronizer.class);
        doThrow(InterruptedException.class).when(result.synchronizer).acquireSharedInterruptibly(1);
        
        result.run();
        assertThrows(ExecutionException.class, result::get);
    }
        
    
    @Test
    void done() {
        var result = new ScheduledBukkitResultTask(() -> {}, "");
        result.setTask(task);
        result.done();
        
        verify(task).cancel();
    }
    
    
    @Test
    void setTask_throws_exception() {
        result.setTask(task);
        assertEquals("BukkitTask has been set", assertThrows(IllegalStateException.class, () -> result.setTask(task)).getMessage());
    }
    
}
