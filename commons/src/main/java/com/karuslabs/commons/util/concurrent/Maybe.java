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

import com.karuslabs.annotations.*;                                            

import java.util.Optional;
import java.util.concurrent.*;

import org.checkerframework.checker.nullness.qual.Nullable;

public final class Maybe<T> extends FutureTask<T> {
    
    private static final Callable<?> CALLABLE = () -> null;
    
    
    public static <T> Maybe<T> value(T value) {
        var maybe =  new Maybe(CALLABLE);
        maybe.set(value);
        
        return maybe;
    }
    
    
    public Maybe(Callable<T> callable) {
        super(callable);
    }

    public Maybe(Runnable runnable, T result) {
        super(runnable, result);
    }
    
    
    @Blocking
    public Optional<T> some() {
        try {
            return Optional.ofNullable(get());
            
        } catch (ExecutionException | InterruptedException ignored) {
            return Optional.empty();
        }
    }
    
    @Blocking
    public Optional<T> some(long timeout, TimeUnit unit) {
        try {
            return Optional.ofNullable(get(timeout, unit));
            
        } catch (ExecutionException | InterruptedException | TimeoutException ignored) {
            return Optional.empty();
        }
    }
    
    
    @Blocking
    public @Nullable T value() {
        try {
            return get();
                    
        } catch (ExecutionException | InterruptedException ignored) {
            return null;
        }
    }
    
    @Blocking
    public @Nullable T value(long timeout, TimeUnit unit) {
        try {
            return get(timeout, unit);
            
        } catch (ExecutionException | InterruptedException | TimeoutException ignored) {
            return null;
        }
    }
    
}
