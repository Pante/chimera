/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.karuslabs.annotations.processor;

import com.karuslabs.annotations.Static;

/**
 * This class consists of static methods that format error messages. 
 */
public @Static class Messages {
    
    /**
     * Creates an error message using the given value and reason.
     * 
     * @param value the subject of the error message
     * @param reason the reason behind the error
     * @return the error message
     */
    public static String format(Object value, String reason) {
        return quote(value) + " " + reason;
    }
    
    /**
     * Creates an error message using the given value, reason and resolution.
     * 
     * @param value the subject of the error message
     * @param reason the reason behind the error
     * @param resolution a short explanation on how to resolve the error
     * @return the error message
     */
    public static String format(Object value, String reason, String resolution) {
        return quote(value) + " " + reason + ", " + resolution;
    }
    
    
    /**
     * Encloses the given value in quotation marks.
     * 
     * @param value the value
     * @return the enclosed value
     */
    public static String quote(Object value) {
        return "\"" + value + "\"";
    }
    
}
