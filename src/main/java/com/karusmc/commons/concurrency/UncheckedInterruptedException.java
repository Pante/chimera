/*
 * Copyright (C) 2016 KarusMC @ karusmc.com 
 * All rights reserved.
 */
package com.karusmc.commons.concurrency;


public class UncheckedInterruptedException extends RuntimeException {
    
    public UncheckedInterruptedException() {}
    
    public UncheckedInterruptedException(String message) {
        super(message);
    }
    
    public UncheckedInterruptedException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public UncheckedInterruptedException(Throwable cause) {
        super(cause);
    }
    
}
