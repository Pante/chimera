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

import java.lang.invoke.*;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import org.checkerframework.checker.nullness.qual.Nullable;


public abstract class ScheduledCallable<T> implements Callable<T> {
    
    static VarHandle CALLBACK;
    
    static {
        try {
            CALLBACK = MethodHandles.lookup().findVarHandle(ScheduledCallable.class, "callback", Callback.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new Error(e);
        }
    }
    
    
    
    public static <T> ScheduledCallable<T> of(Consumer<Callback<T>> consumer) {
        return new ScheduledCallableConsumer<>(consumer);
    }
    
    
    private volatile @Nullable Callback<T> callback;
    
    
    @Override
    public final @Nullable T call() throws Exception {
        var callback  = this.callback;
        if (callback == null) {
            throw new NullPointerException();
        }
        
        run(callback);
        return null;
    }
    
    protected abstract void run(Callback<T> callback);
    
    
    public void set(Callback<T> callback) {
        if (!CALLBACK.compareAndSet(this, null, callback)) {
            throw new IllegalStateException("Callback has been set");
        }
    }
    
}

class ScheduledCallableConsumer<T> extends ScheduledCallable<T> {
    
    private final Consumer<Callback<T>> consumer;
    
    
    ScheduledCallableConsumer(Consumer<Callback<T>> consumer) {
        this.consumer = consumer;
    }

    @Override
    protected void run(Callback<T> callback) {
        consumer.accept(callback);
    }
    
}
