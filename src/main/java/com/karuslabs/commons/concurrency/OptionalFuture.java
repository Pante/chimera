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


public class OptionalFuture<T> extends FutureTask<T> {
    
    public OptionalFuture(Callable<T> callable) {
        super(callable);
    }
    
    public OptionalFuture(Runnable runnable, T result) {
        super(runnable, result);
    }
    
    
    public void ifDone(Consumer<T> consumer) {
        if (isDone()) {
            consumer.accept(getUnchecked());
        }
    }
    
    
    public T getUnchecked() {
        try {
            return get();
            
        } catch (ExecutionException e) {
            throw new UncheckedExecutionException(e);
            
        } catch (InterruptedException e) {
            throw new UncheckedInterruptedException(e);
        }
    }
    
    
    public T getOrDefault(T defaultValue) {
        if (isDone()) {
            return getUnchecked();
        } else {
            return defaultValue;
        }
    }
    
    
    public <E extends RuntimeException> T getOrThrow(Supplier<? extends E> supplier) {
        if (isDone()) {
            return getUnchecked();
            
        } else {
            throw supplier.get();
        }
    }
    
}
