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
import java.util.function.Supplier;


public class ContinuousExecutor extends ScheduledThreadPoolExecutor {
    
    public ContinuousExecutor(int corePoolSize) {
        super(corePoolSize);
    }

    public ContinuousExecutor(int corePoolSize, ThreadFactory threadFactory) {
        super(corePoolSize, threadFactory);
    }
    

    public ContinuousExecutor(int corePoolSize, RejectedExecutionHandler handler) {
        super(corePoolSize, handler);
    }

    public ContinuousExecutor(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, threadFactory, handler);
    }
    
    
    public <V> Continual<V> schedule(Executable<V> executable, long initial, long delay, TimeUnit unit) {
        
    }
    
    public <V> Continual<V> schedule(Executable<V> executable, long initial, long delay, TimeUnit unit, long times) {
        
    }
    
    protected <V> Continual<V> initialise(Executable<V> executable, long initial, long delay, TimeUnit unit, long times) {
        
    }
    
}

class ContinualTask<T> extends Continual<T> {
    
    ContinualTask(Executable<T> executable, long elapsed, long period) {
        this(executable, Continual.INFINITE, elapsed, period);
    }
    
    ContinualTask(Executable<T> executable, long times, long elapsed, long period) {
        super(executable, times, elapsed, period);
    }
    
    
    ContinualTask(Runnable runnable, Supplier<T> supplier, long elapsed, long period) {
        this(runnable, supplier, Continual.INFINITE, elapsed, period);
    }
    
    ContinualTask(Runnable runnable, Supplier<T> supplier, long times, long elapsed, long period) {
        super(runnable, supplier, times, elapsed, period);
    }
    
    
    ContinualTask(Runnable runnable, T result, long elapsed, long period) {
        this(runnable, result, INFINITE, elapsed, period);
    } 
    
    ContinualTask(Runnable runnable, T result, long times, long elapsed, long period) {
        super(runnable, result, times, elapsed, period);
    }
    
}
