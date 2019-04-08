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

import java.util.concurrent.Future;

import org.checkerframework.checker.nullness.qual.Nullable;


/**
 * A repeating asynchronous computation to be used in conjunction with a {@link Repeater}.
 * 
 * @param <T> the result type of the computation
 */
public abstract class Repetition<T> implements Runnable {
    
    /**
     * A constant that denotes infinite.
     */
    public static final int INFINITE = -1;
    
    
    private @Nullable Future<T> context;
    private long times;
    
    
    
    /**
     * Creates a {@code Repetition} to be executed the given number of times.
     * 
     * @param times the number of times this repetition is to be executed
     */
    public Repetition(long times) {
        this.times = times;
    }
    
    
    /**
     * Forwards execution to {@link #run(Future)} and decreases the number of times
     * this {@code Repetition} is to be executed for the stipulated number of times.
     * Execution is then forwarded to {@link #finish(Future)}.
     */
    @Override
    public void run() {
        if (times == INFINITE || --times > INFINITE) {
            run(context);
            
        } else {
            finish(context);
        }
    }
    
    /**
     * Called for each remaining time this {@code Repetition} is to be executed.
     * 
     * @param context the context used to control the execution and result of this
     *                repetition
     */
    protected abstract void run(Future<T> context);
    
    /**
     * Called when this repetition has been executed the stipulated number of times.
     * <br><br>
     * <b>Default implementation:</b>
     * Cancels the execution via the given context.
     * 
     * @param context the context used to control the execution and result of this
     *                repetition
     */
    protected void finish(Future<T> context) {
        context.cancel(false);
    }
    
    /**
     * Sets the context for this {@code Repetition}.
     * 
     * @param context the context used to control the execution and result of this
     *                repetition
     * @throws IllegalStateException if this repetition already has a context
     */
    public void set(Future<T> context) {
        if (this.context == null) {
            this.context = context;
            
        } else {
            throw new IllegalStateException("Context has already been set");
        }
    }
    
}