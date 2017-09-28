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

import java.util.concurrent.ScheduledFuture;
import java.util.function.BiConsumer;
import javax.annotation.Nullable;


public abstract class ScheduledRunnable implements Runnable {
    
    public static ScheduledRunnable of(Runnable runnable, long iterations) {
        return new ScheduledDelegateRunnable(runnable, iterations);
    }
    
    public static ScheduledRunnable of(BiConsumer<Long, Long> consumer, long iterations) {
        return new ScheduledContextRunnable(consumer, iterations);
    }
    
    
    protected @Nullable ScheduledFuture<?> future;
    private long total;
    private long current;
    
    
    public ScheduledRunnable(long iterations) {
        total = iterations;
        current = 0;
    }
    
    
    @Override
    public void run() {
        if (!Thread.interrupted() && current < total) {
            process();
            current++;
            
        } else {
            future.cancel(true);
            Thread.interrupted();
        }
    }
    
    protected abstract void process();
    
    
    public long getIterations() {
        return total;
    }
    
    public long getCurrent() {
        return current;
    }
    
    
    static class ScheduledDelegateRunnable extends ScheduledRunnable {
        
        private final Runnable runnable;
        
        
        ScheduledDelegateRunnable(Runnable runnable, long iterations) {
            super(iterations);
            this.runnable = runnable;
        }

        @Override
        protected void process() {
            runnable.run();
        }
        
    }
    
    static class ScheduledContextRunnable extends ScheduledRunnable {
        
        private final BiConsumer<Long, Long> consumer;
        
        
        ScheduledContextRunnable(BiConsumer<Long, Long> consumer, long iterations) {
            super(iterations);
            this.consumer = consumer;
        }

        @Override
        protected void process() {
            consumer.accept(getCurrent(), getIterations());
        }
        
    }
    
}
