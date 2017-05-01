/*
 * Copyright (C) 2017 Karus Labs
 * All rights reserved.
 */
package com.karuslabs.commons.util;


public class UncheckedIOException extends RuntimeException {
    
    public UncheckedIOException() {}

    public UncheckedIOException(String message) {
        super(message);
    }
    
    public UncheckedIOException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public UncheckedIOException(Throwable cause) {
        super(cause);
    }
    
}
