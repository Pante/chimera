/*
 * Copyright (C) 2016 PanteLegacy @ karusmc.com
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
package com.karusmc.commons.core.xml;


/**
 * Thrown when an unhandled exception occurs while parsing a file.
 */
public class ParserException extends RuntimeException {
    
    /**
     * Constructs this with the specified message.
     * 
     * @param message The error message
     */
    public ParserException(String message) {
        super(message);
    }
    
    /**
     * Constructs this with the specified message and cause.
     * 
     * @param message The error message
     * @param cause The underlying cause
     */
    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
