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

import com.karuslabs.commons.util.concurrent.ResultTask;

import java.lang.invoke.*;
import java.util.concurrent.Callable;

import org.bukkit.scheduler.BukkitTask;

import org.checkerframework.checker.nullness.qual.Nullable;


public class BukkitResultTask<T> extends ResultTask<T> {
    
    static final VarHandle TASK;
    
    static {
        try {
            TASK = MethodHandles.lookup().findVarHandle(BukkitResultTask.class, "task", BukkitTask.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new Error(e);
        }
    }
    
    
    private volatile @Nullable BukkitTask task;
    
    
    public BukkitResultTask(Callable<T> callable) {
        super(callable);
    }

    public BukkitResultTask(Runnable runnable, T result) {
        super(runnable, result);
    }
    
    
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        var scheduled = task;
        if (scheduled == null || !super.cancel(mayInterruptIfRunning)) {
            return false;
        }
        
        try {
            scheduled.cancel();
            return true;
            
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    public void setTask(BukkitTask task) {
        if (!TASK.compareAndSet(this, null, task)) {
            throw new IllegalStateException("BukkitTask has been set");
        }
    }
    
}
