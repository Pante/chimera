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
package com.karuslabs.smoke;

import com.karuslabs.annotations.Static;

import java.util.Collection;

public @Static class Messages {
    
    public static String and(Collection<?> elements) {
        return join(elements, "and");
    }
    
    public static String or(Collection<?> elements) {
        return join(elements, "or");
    }
    
    public static String join(Collection<?> elements, String conjunction) {
        var builder = new StringBuilder();
        
        int i = 0;
        for (var element : elements) {
            builder.append(element);
            if (i <  elements.size() - 2) {
                builder.append(", ");
                
            } else if (i < elements.size() - 1) {
                builder.append(' ').append(conjunction).append(' ');
            }
            
            i++;
        }
        
        return builder.toString();
    }
    
    
    public static String format(Object value, String reason) {
        return quote(value) + " " + reason;
    }
    
    public static String format(Object value, String reason, String resolution) {
        return quote(value) + " " + reason + ", " + resolution;
    }
    
    
    public static String quote(Object value) {
        return "\"" + value + "\"";
    }
    
}
