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

import java.util.concurrent.Callable;
import java.util.function.Consumer;


public abstract class ScheduledExecutor {
    
        
    public <T> Result<T> submit(Callable<T> callable) {
        return submit(callable, 0L);
    }
        
    public <T> Result<T> submit(Callable<T> callable, long delay) {
        return submit(new ResultTask<>(callable), delay);
    }
    
    
    public <T> Result<T> submit(Runnable runnable, T result) {
        return submit(runnable, result, 0L);
    }
        
    public <T> Result<T> submit(Runnable runnable, T result, long delay) {
        return submit(new ResultTask<>(runnable, result), delay);
    }
    
    
    public Result<?> submit(Runnable runnable) {
        return submit(runnable, 0L);
    }
    
    public Result<?> submit(Runnable runnable, long delay) {
        return submit(new ResultTask<>(runnable, null), delay);
    }
    
    
    protected abstract <T> Result<T> submit(ResultTask<T> task, long delay);
    
    
    public <T> Result<T> schedule(Consumer<Callback> consumer, long period) {
        return schedule(consumer, period, 0L);
    }
    
    public <T> Result<T> schedule(Consumer<Callback> consumer, long period, long delay) {
        return schedule(new ScheduledResultTask<>(consumer), period, delay);
    }
    
    
    public <T> Result<T> schedule(Runnable runnable, T result, long period) {
        return schedule(runnable, result, period, 0L);
    }
    
    public <T> Result<T> schedule(Runnable runnable, T result, long period, long delay) {
        return schedule(new ScheduledResultTask<>(runnable, result), period, delay);
    }
    
    
    public Result<?> schedule(Runnable runnable, long period) {
        return schedule(runnable, period, 0L);
    }
    
    public Result<?> schedule(Runnable runnable, long period, long delay) {
        return schedule(new ScheduledResultTask<>(runnable, null), period, delay);
    }
    
    
    protected abstract <T> Result<T> schedule(ScheduledResultTask<T> task, long period, long delay);
    
}
