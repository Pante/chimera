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


public abstract class Repetition<T> implements Runnable {
    
    public static final int INFINITE = -1;
    
    
    private @Nullable Future<T> context;
    private long times;
    
    
    public Repetition(long times) {
        this.times = times;
    }
    
    
    /**
     * Associates the specified button with the specified slot if the {@code Region}
     * to which these {@code Buttons} are bound contains the specified slot.
     * 
     * @param slot the slot
     * @param button the button
     * @return the previous button associated with the specified slot
     * @throws IllegalArgumentException if the region to which these buttons are bound does not contain the specified slot
     */
    @Override
    public void run() {
        if (times == INFINITE || --times > INFINITE) {
            run(context);
            
        } else {
            finish(context);
        }
    }
    
    protected abstract void run(Future<T> context);
    
    protected void finish(Future<T> context) {
        context.cancel(false);
    }
    
    public void set(Future<T> context) {
        if (this.context == null) {
            this.context = context;
            
        } else {
            throw new IllegalStateException("Context has already been set");
        }
    }
    
}