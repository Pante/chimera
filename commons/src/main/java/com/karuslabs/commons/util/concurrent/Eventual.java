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

import java.util.Optional;
import java.util.concurrent.*;

import org.checkerframework.checker.nullness.qual.Nullable;


/**
 * A result of an asynchronous computation with additional retrieval methods.
 * 
 * @param <T> the result type of the computation
 */
public interface Eventual<T> extends Future<T> {
    
    /**
     * Waits if necessary for the computation to complete, and then retrieves its result.
     * An empty optional is returned if either a {@code ExecutionException} or
     * {@code InterruptedException} was thrown.
     * 
     * @return the computed result, or an empty optional if an exception was thrown
     */
    public default Optional<T> acquire() {
        try {
            return Optional.ofNullable(get());
            
        } catch (ExecutionException | InterruptedException ignored) {
            return Optional.empty();
        }
    }
    
    /**
     * Waits if necessary for at most the given time for the computation to complete, 
     * and then retrieves its result, if available. An empty optional is returned 
     * if either a {@code ExecutionException} or {@code InterruptedException} was 
     * thrown.
     *
     * @param timeout the maximum time to wait
     * @param unit the time unit of the timeout argument
     * @return the computed result, or an empty optional if an exception was thrown
     */
    public default Optional<T> acquire(long timeout, TimeUnit unit) {
        try {
            return Optional.ofNullable(get(timeout, unit));
            
        } catch (ExecutionException | InterruptedException | TimeoutException ignored) {
            return Optional.empty();
        }
    }
    
    
    /**
     * Waits if necessary for the computation to complete, and then retrieves its result.
     * Returns null if either a {@code ExecutionException} or {@code InterruptedException} was thrown.
     * 
     * @return the computed result, or null if an exception was thrown
     */
    public default @Nullable T await() {
        try {
            return get();
                    
        } catch (ExecutionException | InterruptedException ignored) {
            return null;
        }
    }
    
    /**
     * Waits if necessary for at most the given time for the computation to complete, 
     * and then retrieves its result, if available. Returns null if either a {@code ExecutionException} 
     * or {@code InterruptedException} was thrown.
     *
     * @param timeout the maximum time to wait
     * @param unit the time unit of the timeout argument
     * @return the computed result, or null if an exception was thrown
     */
    public default @Nullable T await(long timeout, TimeUnit unit) {
        try {
            return get(timeout, unit);
            
        } catch (ExecutionException | InterruptedException | TimeoutException ignored) {
            return null;
        }
    }
    
}
