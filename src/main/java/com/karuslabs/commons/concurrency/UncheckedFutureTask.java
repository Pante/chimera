/*
 * Copyright (C) 2017 Karus Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.karuslabs.commons.concurrency;

import java.util.concurrent.*;


/**
 * Represents a FutureTask with convenience methods to retrieve the result.
 * 
 * @param <T> The computation result type
 */
public class UncheckedFutureTask<T> extends FutureTask<T> {
    
    /**
     * Constructs this that will, upon running, execute the given Callable.
     * 
     * @param callable the callable task
     */
    public UncheckedFutureTask(Callable<T> callable) {
        super(callable);
    }
    
    /**
     * Constructs this that will, upon running, execute the given Runnable, and arrange that get will return the given result on successful completion.
     * 
     * @param runnable the runnable task
     * @param result the result to return on successful completion.
     */
    public UncheckedFutureTask(Runnable runnable, T result) {
        super(runnable, result);
    }
    
    
    /**
     * Waits if necessary for the computation to complete, and then retrieves its result.
     * Wraps any thrown {@link java.util.concurrent.ExecutionException}s and {@link java.lang.InterruptedException}s in {@link UncheckedExecutionException}s and {@link UncheckedInterruptedException}s.
     * 
     * @return the computed result
     */
    public T getUnchecked() {
        try {
            return get();
            
        } catch (ExecutionException e) {
            throw new UncheckedExecutionException(e);
            
        } catch (InterruptedException e) {
            throw new UncheckedInterruptedException(e);
        }
    }
    
    
    /**
     * Returns the computation result, or the given default value if the computation has not completed yet.
     * 
     * @param defaultValue the value to return if the computation has yet to be completed
     * @return the computation result, if completed; else the default value
     */
    public T getOrDefault(T defaultValue) {
        if (isDone()) {
            return getUnchecked();
        } else {
            return defaultValue;
        }
    } 
    
}
