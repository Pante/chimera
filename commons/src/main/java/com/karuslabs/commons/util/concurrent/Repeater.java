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


public class Repeater extends ScheduledThreadPoolExecutor {
    
    public Repeater(int corePoolSize) {
        super(corePoolSize);
    }

    public Repeater(int corePoolSize, ThreadFactory threadFactory) {
        super(corePoolSize, threadFactory);
    }
    

    public Repeater(int corePoolSize, RejectedExecutionHandler handler) {
        super(corePoolSize, handler);
    }

    public Repeater(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, threadFactory, handler);
    }
    
    
    public ScheduledFuture<?> schedule(Runnable runnable, long initial, long period, TimeUnit unit, long times) {
        return schedule(new RunnableRepetition(runnable, times), initial, period, unit);
    }
    
    public ScheduledFuture<?> schedule(Repetition<?> continual, long initial, long period, TimeUnit unit) {
        return scheduleAtFixedRate(continual, initial, period, unit);
    }
    
    @Override
    protected <V> RunnableScheduledFuture<V> decorateTask(Runnable runnable, RunnableScheduledFuture<V> task) {
        if (runnable instanceof Repetition) {
            var repetition = (Repetition<V>) runnable;
            repetition.set(task);
        }
        
        return task;
    }
    
}


