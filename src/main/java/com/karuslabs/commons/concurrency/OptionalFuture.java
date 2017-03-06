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
import java.util.function.*;


/**
 * Represents an implementation of both an {@link java.util.Optional} and {@link java.util.concurrent.FutureTask}.
 * 
 * @param <T> the type of the computation
 */
public class OptionalFuture<T> extends FutureTask<T> {
    
    /**
     * Creates a new <code>OptionalFuture</code> which will, upon running, execute the given <code>Callable</code>.
     * 
     * @param callable the callable task
     */
    public OptionalFuture(Callable<T> callable) {
        super(callable);
    }
    
    /**
     * Creates a new <code>OptionalFuture</code> which will, upon running, execute the given <code>Runnable</code>, 
     * and arrange that {@link #get()} will return the given result on successful completion.
     * 
     * @param runnable the runnable task
     * @param result the result to return on successful completion
     */
    public OptionalFuture(Runnable runnable, T result) {
        super(runnable, result);
    }
    
    
    /**
     * Invoke the specified consumer with the value if the computation is completed; else do nothing.
     * 
     * @param consumer block to be executed if the computation has completed
     */
    public void ifDone(Consumer<T> consumer) {
        if (isDone()) {
            consumer.accept(getUnchecked());
        }
    }
    
    
    /**
     * Waits if necessary for the computation to complete, and then retrieves its result, 
     * transforming any checked exceptions thrown to their unchecked variants.
     * 
     * @see UncheckedExecutionException
     * @see UncheckedInterruptedException
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
     * Returns the computation result, if the computation has completed; else <code>null</code>.
     * 
     * @return the computation result, if completed; else null
     */
    public T getIfDone() {
        if (isDone()) {
            return getUnchecked();
            
        } else {
            return null;
        }
    }

    
    /**
     * Returns the computation result, if the computation has completed; else the default value
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
    
    
    /**
     * Return the computation, if completed; else throw an unchecked exception to be created by the provided <code>supplier</code>.
     * 
     * @param supplier the supplier which will return the unchecked exception to be thrown
     * @return the completed computation
     * @throws RuntimeException or any other unchecked exceptions specified by the suppiler
     */
    public T getOrThrow(Supplier<? extends RuntimeException> supplier) {
        if (isDone()) {
            return getUnchecked();
            
        } else {
            throw supplier.get();
        }
    }
    
}
