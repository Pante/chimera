/*
 * Copyright (C) 2016 KarusMC @ karusmc.com 
 * All rights reserved.
 */
package com.karusmc.commons.concurrency;

import java.util.concurrent.*;


public class UncheckedFutureTask<T> extends FutureTask<T> {
    
    public UncheckedFutureTask(Callable<T> callable) {
        super(callable);
    }
    
    public UncheckedFutureTask(Runnable runnable, T result) {
        super(runnable, result);
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
    
}
