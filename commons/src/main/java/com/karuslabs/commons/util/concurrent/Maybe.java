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

/**
 * A cancellable result of an asynchronous computation with additional retrieval 
 * methods.
 * 
 * @param <T> the type of the result
 */
public final class Maybe<T> extends FutureTask<T> {
    
    private static final Callable<?> CALLABLE = () -> null;
    
    /**
     * Creates a completed {@code Maybe} that immediately returns the given value.
     * 
     * @param <T> the type of the result
     * @param value the result.
     * @return a completed {@code Maybe} with the given result
     */
    public static <T> Maybe<T> value(T value) {
        var maybe =  new Maybe(CALLABLE);
        maybe.set(value);
        
        return maybe;
    }
    
    /**
     * Creates a {@code Maybe} that will, upon running execute the given callable.
     * 
     * @param callable the callable task
     */
    public Maybe(Callable<T> callable) {
        super(callable);
    }

    /**
     * Creates a {@code Maybe} that will, upon running, execute the given runnable,
     * and result on successful completion.
     * 
     * @param runnable the runnable task
     * @param result the result to return on successful completion.
     */
    public Maybe(Runnable runnable, T result) {
        super(runnable, result);
    }
    
    /**
     * Waits if necessary for the computation to complete, and then retrieves its
     * result.
     * 
     * @return the computed result, or an empty {@code Optional} if an exception was thrown
     */
    @Blocking
    public Optional<T> some() {
        try {
            return Optional.ofNullable(get());
            
        } catch (ExecutionException | InterruptedException ignored) {
            return Optional.empty();
        }
    }
    
    /**
     * Waits if necessary for at most the given time for the computation to complete, 
     * and then retrieves its result, if available.
     * 
     * @param timeout the maximum time to wait
     * @param unit the time unit of the timeout argument
     * @return the computed result, or an empty {@code Optional} if an exception 
     *         was thrown
     */
    @Blocking
    public Optional<T> some(long timeout, TimeUnit unit) {
        try {
            return Optional.ofNullable(get(timeout, unit));
            
        } catch (ExecutionException | InterruptedException | TimeoutException ignored) {
            return Optional.empty();
        }
    }
    
    /**
     * Waits if necessary for the computation to complete, and then retrieves its
     * result.
     * 
     * @return the computed result, or {@code null} if an exception was thrown
     */
    @Blocking
    public @Nullable T value() {
        try {
            return get();
                    
        } catch (ExecutionException | InterruptedException ignored) {
            return null;
        }
    }
    
    /**
     * Waits if necessary for at most the given time for the computation to complete, 
     * and then retrieves its result, if available.
     *
     * @param timeout the maximum time to wait
     * @param unit the time unit of the timeout argument
     * @return the computed result, or {@code null} if an exception was thrown
     */
    @Blocking
    public @Nullable T value(long timeout, TimeUnit unit) {
        try {
            return get(timeout, unit);
            
        } catch (ExecutionException | InterruptedException | TimeoutException ignored) {
            return null;
        }
    }
    
}
