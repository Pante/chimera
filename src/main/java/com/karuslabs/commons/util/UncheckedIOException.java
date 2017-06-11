/*
 * Copyright (C) 2017 Karus Labs
 * All rights reserved.
 */
package com.karuslabs.commons.util;


/**
 * Unchecked version of {@link java.io.IOException}.
 */
public class UncheckedIOException extends RuntimeException {
    
    
    /**
     * Constructs an <code>UncheckedIOException</code> with null as its error detail message.
     */
    public UncheckedIOException() {}

    /**
     * Constructs an <code>UncheckedIOException</code> with the specified detail message.
     * 
     * @param message the detail message
     */
    public UncheckedIOException(String message) {
        super(message);
    }
    
    /**
     * Constructs an <code>UncheckedIOException</code> with the specified detail message and cause. 
     * 
     * @param message the detail message
     * @param cause the cause
     */
    public UncheckedIOException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Constructs an <code>UncheckedIOException</code> with the specified cause and a detail message of <code>(cause==null ? null : cause.toString())</code>.
     * 
     * @param cause the cause
     */
    public UncheckedIOException(Throwable cause) {
        super(cause);
    }
    
}
