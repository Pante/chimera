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
package com.karuslabs.puff;

import com.karuslabs.annotations.Static;

import java.util.Collection;
import java.util.function.BiConsumer;

public @Static class Texts {
    
    public static BiConsumer<String, StringBuilder> STRING = (string, builder) -> builder.append(string);
    
    public static <T> String and(Collection<? extends T> elements, BiConsumer<T, StringBuilder> format) {
        return join(elements, format, "and");
    }
    
    public static <T> String and(T[] elements, BiConsumer<T, StringBuilder> format) {
        return join(elements, format, "and");
    }
    
    
    public static <T> String or(Collection<? extends T> elements, BiConsumer<T, StringBuilder> format) {
        return join(elements, format, "or");
    }
    
    public static <T> String or(T[] elements, BiConsumer<T, StringBuilder> format) {
        return join(elements, format, "or");
    }
    
    
    public static <T> String join(Collection<? extends T> elements, BiConsumer<T, StringBuilder> format, String conjunction) {
        var builder = new StringBuilder();
        join(elements, builder, format, conjunction);
        return builder.toString();
    }
    
    public static <T> String join(T[] elements, BiConsumer<T, StringBuilder> format, String conjunction) {
        var builder = new StringBuilder();
        join(elements, builder, format, conjunction);
        return builder.toString();
    }
    
    
    public static <T> void join(Collection<? extends T> elements, StringBuilder builder, BiConsumer<T, StringBuilder> format, String conjunction) {
        int i = 0;
        for (var element : elements) {
            format.accept(element, builder);
            if (i <  elements.size() - 2) {
                builder.append(", ");
                
            } else if (i < elements.size() - 1) {
                builder.append(' ').append(conjunction).append(' ');
            }
            
            i++;
        }
    }
    
    public static <T> void join(T[] elements, StringBuilder builder, BiConsumer<T, StringBuilder> format, String conjunction) {
        for (var i = 0; i < elements.length; i++) {
            format.accept(elements[i], builder);
            if (i <  elements.length - 2) {
                builder.append(", ");
                
            } else if (i < elements.length - 1) {
                builder.append(' ').append(conjunction).append(' ');
            }
        }
    }
    
    public static String join(String first, String separator, String second) {
        if (first.isBlank()) {
            return second;
            
        } else if (second.isBlank()) {
            return first;
            
        } else {
            return first + separator + second;
        }
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
    
    public static String plural(String noun) {
        if (noun.isBlank()) {
            return noun;
            
        } else if (noun.endsWith("s") || noun.endsWith("sh") || noun.endsWith("ch") || noun.endsWith("x") || noun.endsWith("z")) {
            return noun + "es";
            
        } else {
            return noun + "s";
        }
    }
    
}
