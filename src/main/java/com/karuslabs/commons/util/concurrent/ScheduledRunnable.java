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

import java.util.function.Consumer;


public abstract class ScheduledRunnable extends CancellableRunnable implements ScheduledCancellable {
        
    public static ScheduledRunnable of(Consumer<ScheduledCancellable> consumer, long iterations) {
        return new ScheduledConsumer(consumer, iterations);
    }
    
    public static ScheduledRunnable of(Runnable runnable, long iterations) {
        return new DelegateRunnable(runnable, iterations);
    }
    
    
    private long total;
    private long current;
    
    
    public ScheduledRunnable(long iterations) {
        total = iterations;
        current = 0;
    }
    
    
    @Override
    public void run() {
        if (!Thread.interrupted() && (total == -1 || current < total)) {
            process();
            if (total != -1) {
                current++;
            }
            
        } else {
            cancel();
        }
    }
    
    protected abstract void process();
    
    
    @Override
    public long getIterations() {
        return total;
    }
    
    @Override
    public long getCurrent() {
        return current;
    }

    
    static class ScheduledConsumer extends ScheduledRunnable {
        
        private final Consumer<ScheduledCancellable> consumer;
        
        
        ScheduledConsumer(Consumer<ScheduledCancellable> consumer, long iterations) {
            super(iterations);
            this.consumer = consumer;
        }

        @Override
        protected void process() {
            consumer.accept(this);
        }
        
    }  
    
    static class DelegateRunnable extends ScheduledRunnable {
        
        private final Runnable runnable;
        
        
        DelegateRunnable(Runnable runnable, long iterations) {
            super(iterations);
            this.runnable = runnable;
        }

        @Override
        protected void process() {
            runnable.run();
        }
        
    }
    
}
