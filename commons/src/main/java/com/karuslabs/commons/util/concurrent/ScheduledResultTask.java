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

import org.checkerframework.checker.nullness.qual.Nullable;


public class ScheduledResultTask<T> extends ResultTask<T> implements Callback<T> {
    
    public static final long INFINITE = -1;
    
    
    public static <T> ScheduledResultTask<T> of(Consumer<Callback<T>> consumer) {
        return of(consumer, INFINITE);
    }
    
    public static <T> ScheduledResultTask<T> of(Consumer<Callback<T>> consumer, long count) {
        return of(ScheduledCallable.of(consumer), count);
    }
    
    
    public static <T> ScheduledResultTask<T> of(ScheduledCallable<T> callable) {
        return of(callable, INFINITE);
    }
    
    public static <T> ScheduledResultTask<T> of(ScheduledCallable<T> callable, long count) {
        var result = new ScheduledResultTask<>(callable, count);
        callable.set(result);
        return result;
    }
    
    
    @Nullable T result;
    volatile long count;

    
    public ScheduledResultTask(Runnable runnable, T result) {
        this(runnable, result, INFINITE);
    }
    
    public ScheduledResultTask(Runnable runnable, T result, long count) {
        super(runnable, result);
        this.result = result;
        this.count = count;
    }
    
    protected ScheduledResultTask(Callable<T> callable, long count) {
        super(callable);
        this.count = count;
    }
    
    
    @Override
    public void run() {
        if (runAndReset() && count > 0 && --count == 0) {
            set(result);
        }
    }

    
    @Override
    public void set(T result) {
        super.set(result);
    }
    
    @Override
    public void exception(Throwable throwable) {
        super.setException(throwable);
    }

    @Override
    public boolean cancel() {
        return cancel(false);
    }

    @Override
    public long count() {
        return count;
    }
    
}