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


public abstract class ScheduledResultTask<T> extends ResultTask<T> implements Repeatable {
    
    public static <T> ScheduledResultTask<T> of(Runnable runnable, T value, long iterations) {
        return new ScheduledPromiseRunnable<>(runnable, value, iterations);
    }
    
    
    public static final int INFINITE = -1;
    
    long current;
    long total;
    
    
    public ScheduledResultTask(long iterations) {
        current = 0;
        total = iterations;
    }
    
    
    @Override
    public void run() {
        if (total == -1 || current < total) {
            try {
                process();
                if (total != -1) {
                    current++;
                }
            } catch (Exception e) {
                thrown = e;
                done();
            }
            
        } else {
            done();
        }
    }
    
    @Override
    void finish(int updated) {
        super.finish(updated);
        callback();
    }
    
    protected void callback() {};
    

    @Override
    public long getCurrent() {
        return current;
    }

    @Override
    public long getIterations() {
        return total;
    }
    
    
    static class ScheduledPromiseRunnable<T> extends ScheduledResultTask<T> {
        
        private final Runnable runnable;
        private final T value;
        
        ScheduledPromiseRunnable(Runnable runnable, T value, long iterations) {
            super(iterations);
            this.runnable = runnable;
            this.value = value;
        }
        
        @Override
        protected void process() throws Exception {
            runnable.run();
        }

        @Override
        protected T value() {
            return value;
        }
        
    }
    
}
