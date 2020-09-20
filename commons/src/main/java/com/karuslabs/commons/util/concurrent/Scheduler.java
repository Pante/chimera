/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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

import com.karuslabs.annotations.Lazy;

import java.util.concurrent.*;
import java.util.function.Consumer;

public class Scheduler extends ScheduledThreadPoolExecutor {
    
    public Scheduler(int corePoolSize) {
        super(corePoolSize);
    }

    public Scheduler(int corePoolSize, ThreadFactory threadFactory) {
        super(corePoolSize, threadFactory);
    }
    

    public Scheduler(int corePoolSize, RejectedExecutionHandler handler) {
        super(corePoolSize, handler);
    }

    public Scheduler(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, threadFactory, handler);
    }
    
    
    public ScheduledFuture<?> schedule(Consumer<Context> task, long initial, long period, TimeUnit unit) {
        return schedule(task, initial, period, unit, Context.INFINITE);
    }
    
    public ScheduledFuture<?> schedule(Consumer<Context> task, long initial, long period, TimeUnit unit, long times) {
        return scheduleAtFixedRate(new RunnableContext(task, times), initial, period, unit);
    }
    
    @Override
    protected <V> RunnableScheduledFuture<V> decorateTask(Runnable runnable, RunnableScheduledFuture<V> future) {
        if (runnable instanceof RunnableContext) {
            ((RunnableContext) runnable).future = future;
        }
        
        return future;
    }
    
}

class RunnableContext implements Context, Runnable {
    
    private final Consumer<Context> task;
    @Lazy Future<?> future;
    volatile long times;
    
    RunnableContext(Consumer<Context> task, long times) {
        this.task = task;
        this.times = times;
    }
    
    @Override
    public void run() {
        if (times == INFINITE || times > 0) {
            task.accept(this);
            if (times > 0) {
                times--;
            }
            
        } else {
            cancel();
        }
    }
    
    @Override
    public void cancel() {
        future.cancel(false);
    }

    @Override
    public long times() {
        return times;
    }
    
}


