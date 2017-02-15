/*
 * Copyright (C) 2017 PanteLegacy @ karusmc.com
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
package com.karusmc.commons.concurrency;


/**
 * Thrown when an unhandled {@link java.lang.InterruptedException} occurs when calling {@link UncheckedFutureTask#getUnchecked()}.
 */
public class UncheckedInterruptedException extends RuntimeException {
    
    /**
     * Constructs this with no detail message.
     */
    public UncheckedInterruptedException() {}
    
    /**
     * Constructs this with the specified detail message.
     * 
     * @param message the detail message
     */
    public UncheckedInterruptedException(String message) {
        super(message);
    }
    
    /**
     * Constructs this with the specified detail message and cause.
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
