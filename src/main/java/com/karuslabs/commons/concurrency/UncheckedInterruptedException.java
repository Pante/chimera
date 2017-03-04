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


/**
 * Unchecked variant of {@link java.lang.InterruptedException}.
 * @see OptionalFuture
 */
public class UncheckedInterruptedException extends RuntimeException {
    
    /**
     * Creates a new exception with no detail message.
     */
    public UncheckedInterruptedException() {}
    
    /**
     * Creates a new exception with the detail message specified.
     * 
     * @param message the detail message
     */
    public UncheckedInterruptedException(String message) {
        super(message);
    }
    
    /**
     * Creates a new exception with the detail message and cause specified.
     * 
     * @param message the detail message
     * @param cause the cause
     */
    public UncheckedInterruptedException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Constructs this with the specified cause.
     * 
     * @param cause the cause
     */
    public UncheckedInterruptedException(Throwable cause) {
        super(cause);
    }
    
}
