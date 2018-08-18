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
import static com.karuslabs.commons.util.concurrent.ScheduledResultTask.INFINITE;

import java.lang.invoke.*;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import org.bukkit.scheduler.BukkitTask;

import org.checkerframework.checker.nullness.qual.Nullable;


public class ScheduledBukkitResultTask<T> extends ScheduledResultTask<T> {
    
    static final VarHandle TASK;
    
    static {
        try {
            TASK = MethodHandles.lookup().findVarHandle(ScheduledBukkitResultTask.class, "task", BukkitTask.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new Error(e);
        }
    }
    
    
    public static <T> ScheduledBukkitResultTask<T> of(Consumer<Callback<T>> consumer) {
        return of(consumer, INFINITE);
    }
    
    public static <T> ScheduledBukkitResultTask<T> of(Consumer<Callback<T>> consumer, long count) {
        var callable = ScheduledCallable.of(consumer);
        var result = new ScheduledBukkitResultTask<>(callable, count);
        callable.set(result);
        return result;
    }
    
    public static <T> ScheduledResultTask<T> of(ScheduledCallable<T> callable) {
        return of(callable, INFINITE);
    }
    
    public static <T> ScheduledResultTask<T> of(ScheduledCallable<T> callable, long count) {
        var result = new ScheduledBukkitResultTask<>(callable, count);
        callable.set(result);
        return result;
    }
    
    
    private BinarySynchronizer synchronizer;
    private volatile @Nullable BukkitTask task;
    
    
    public ScheduledBukkitResultTask(Runnable runnable, T result) {
        this(runnable, result, INFINITE);
    }
    
    public ScheduledBukkitResultTask(Runnable runnable, T result, long count) {
        super(runnable, result, count);
        this.synchronizer = new BinarySynchronizer();
    }
    
    public ScheduledBukkitResultTask(Callable<T> callable, long count) {
        super(callable, count);
    }
    
    
    @Override
    public void run() {
        try {
            synchronizer.acquireSharedInterruptibly(1);
            super.run();
            
        } catch (InterruptedException e) {
            setException(e);
        } 
    }
    
    @Override
    protected void done() {
        try {
            synchronizer.acquireShared(1);
            
        } finally {
            var scheduled = task;
            if (scheduled != null) {
                scheduled.cancel();
            }
        }
    }
    
    
    public void setTask(BukkitTask task) {
        if (TASK.compareAndSet(this, null, task)) {
            synchronizer.releaseShared(1);
            
        } else {
            throw new IllegalStateException("Task has been set");
        }
    }
    
}
