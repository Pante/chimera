/*
 * Copyright (C) 2016 KarusMC @ karusmc.com 
 * All rights reserved.
 */
package com.karusmc.commons.concurrency;


public class UncheckedExecutionException extends RuntimeException {
    
    public UncheckedExecutionException() {}
    
    public UncheckedExecutionException(String message) {
        super(message);
    }
    
    public UncheckedExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public UncheckedExecutionException(Throwable cause) {
        super(cause);
    }
    
}
