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

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import javax.annotation.Nullable;


public abstract class CancellableRunnable implements Runnable, Cancellable {
    
    public static CancellableRunnable of(Consumer<Cancellable> consumer) {
        return new CancellableConsumer(consumer);
    }
    
    public static CancellableRunnable of(Runnable runnable) {
        return new DelegateRunnable(runnable);
    }
    
    
    private @Nullable AtomicReference<Future<?>> reference;
    
    
    public CancellableRunnable() {
        reference = new AtomicReference<>();
    }
    
    
    protected @Nullable Future<?> get() {
        return reference.get();
    }
    
    protected void set(Future<?> future) {
        if (!reference.compareAndSet(null, future)) {
            throw new IllegalStateException("Future has already been set");
        }
    }
    
    
    @Override
    public void cancel() {
        reference.getAndSet(null).cancel(true);
    }
    
    
    static class CancellableConsumer extends CancellableRunnable {

        private final Consumer<Cancellable> consumer;
        
        CancellableConsumer(Consumer<Cancellable> consumer) {
            this.consumer = consumer;
        }
        
        @Override
        public void run() {
            consumer.accept(this);
        }
        
    }
    
    static class DelegateRunnable extends CancellableRunnable {
        
        private final Runnable runnable;
        
        DelegateRunnable(Runnable runnable) {
            this.runnable = runnable;
        }
        
        @Override
        public void run() {
            runnable.run();
        }
        
    }
    
}
