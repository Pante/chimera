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

import com.karuslabs.commons.annotation.Blocking;

import java.util.concurrent.*;
import javax.annotation.*;


/**
 * A {@code Result} represents the result of an asynchronous computation.
 * In addition to {@code get()}, {@code Result} contains additional convenience
 * methods for retrieving the result of an asynchronous computation.
 * 
 * @param <T> the result type of the asynchronous computation
 */
public interface Result<T> extends Future<T> {
    
    /**
     * Wraps the specified {@link Future} with a {@code Result}.
     * 
     * @param <T> the result type of the asynchronous computation
     * @param future the Future
     * @return a Result which wraps the specified future
     */
    public static <T> Result<T> of(Future<T> future) {
        return new ProxiedResult<>(future);
    }
    
    
    /**
     * Waits if necessary for the computation to complete, and then
     * returns the result, or {@code null} if a {@code CancellationException}
     * was thrown.
     * 
     * @return the result of the computation, or null if a CancellationException was thrown.
     * @throws UncheckedExecutionException if the computation threw an exception
     * @throws UncheckedInterruptedException if the current thread was interrupted while waiting
     */
    @Blocking
    public default @Nullable T await() {
        try {
            return obtain();
            
        } catch (CancellationException e) {
            return null;
        }
    }
    
    /**
     * Waits if necessary for at most the given time for the computation
     * to complete, and then returns the result, or {@code null} if either the result is not available or 
     * a {@code CancellationException} was thrown.
     *
     * @param timeout the maximum time to wait
     * @param unit the time unit of the timeout argument
     * @return the computed result, or null
     * @throws UncheckedExecutionException if the computation threw an exception
     * @throws UncheckedInterruptedException if the current thread was interrupted while waiting
     */
    @Blocking
    public default @Nullable T await(long timeout, TimeUnit unit) {
        try {
            return get(timeout, unit);
            
        } catch (CancellationException | TimeoutException e) {
            return null;
            
        } catch (InterruptedException e) {
            throw new UncheckedInterruptedException(e);
            
        } catch (ExecutionException e) {
            throw new UncheckedExecutionException(e);
        }
    }
    
    
    /**
     * Waits if necessary for the computation to complete, and then retrieves its result.
     *
     * @return the computed result
     * @throws CancellationException if the computation was cancelled
     * @throws UncheckedExecutionException if the computation threw an exception
     * @throws UncheckedInterruptedException if the current thread was interrupted while waiting
     */
    @Blocking
    public default @Nullable T obtain() {
        try {
            return get();
            
        } catch (InterruptedException e) {
            throw new UncheckedInterruptedException(e);
            
        } catch (ExecutionException e) {
            throw new UncheckedExecutionException(e);
        }
    }
    
    /**
     * Waits if necessary for at most the given time for the computation to complete, 
     * and then retrieves its result, if available.
     *
     * @param timeout the maximum time to wait
     * @param unit the time unit of the timeout argument
     * @return the computed result
     * @throws CancellationException if the computation was cancelled
     * @throws UncheckedExecutionException if the computation threw an exception
     * @throws UncheckedInterruptedException if the current thread was interrupted while waiting
     * @throws UncheckedTimeoutException if the wait timed out
     */
    @Blocking
    public default @Nullable T obtain(long timeout, TimeUnit unit) {
        try {
            return get(timeout, unit);
            
        } catch (InterruptedException e) {
            throw new UncheckedInterruptedException(e);
            
        } catch (ExecutionException e) {
            throw new UncheckedExecutionException(e);
            
        } catch (TimeoutException e) {
            throw new UncheckedTimeoutException(e);
        }
    }
    
}
