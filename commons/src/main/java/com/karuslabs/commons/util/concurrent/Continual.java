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

import java.util.function.Supplier;

import org.checkerframework.checker.nullness.qual.Nullable;


public class Continual<T> extends EventualTask<T> {    
    
    private static final long INFINITE = -1;
    
    private @Nullable Supplier<T> supplier;
    private @Nullable T result;
    private long times;
    
    
    public Continual(Callback<T> callback) {
        this(callback, INFINITE);
    }
    
    public Continual(Callback<T> callback, long times) {
        super(callback, null);
        this.times = times;
    }
    
    
    public Continual(Runnable runnable, Supplier<T> supplier) {
        this(runnable, supplier, INFINITE);
    }
    
    public Continual(Runnable runnable, Supplier<T> supplier, long times) {
        super(runnable, null);
        this.supplier = supplier;
        this.times = times;
    }
    
    
    public Continual(Runnable runnable, T result) {
        this(runnable, result, INFINITE);
    } 
    
    public Continual(Runnable runnable, T result, long times) {
        super(runnable, null);
        this.result = result;
        this.times = times;
    }
    
    
    @Override
    public void run() {
        if (times == 0 || (runAndReset() && times > 0 && --times == 0)) {
            if (supplier != null) {
                set(supplier.get());
                
            } else if (result != null) {
                set(result);
            }
        }
    }
    
    
    public long times() {
        return times;
    }
    
}
