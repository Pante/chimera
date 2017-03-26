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
package com.karuslabs.commons.util.concurrent;

import java.util.concurrent.*;


public class DefaultableTask<T> extends FutureTask<T> {
    
    private T defaultValue;
            
    
    public DefaultableTask(Callable<T> callable, T defaultValue) {
        super(callable);
        if (defaultValue != null) {
            this.defaultValue = defaultValue;
            
        } else {
            throw new NullPointerException();
        }
    }
        
    public DefaultableTask(Runnable runnable, T result, T defaultValue) {
        super(runnable, result);
        if (defaultValue != null) {
            this.defaultValue = defaultValue;
            
        } else {
            throw new NullPointerException();
        }
    }
    
    
    @Override
    public T get() throws InterruptedException, ExecutionException {
        if (isDone() && super.get() != null) {
            return super.get();
            
        } else {
            return defaultValue;
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
    
}
