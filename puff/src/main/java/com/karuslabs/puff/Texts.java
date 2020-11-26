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
    
    public static final BiConsumer<String, StringBuilder> STRING = (string, builder) -> builder.append(string);
    public static final BiConsumer<Object, StringBuilder> SCREAMING_CASE = (object, builder) -> builder.append(object.toString().toLowerCase().replace('_', ' '));
    
    
    public static <T> String and(Collection<? extends T> elements, BiConsumer<? super T, StringBuilder> consumer) {
        return conjunction(elements, consumer, "and");
    }
    
    public static <T> String and(T[] elements, BiConsumer<? super T, StringBuilder> consumer) {
        return conjunction(elements, consumer, "and");
    }
    
    
    public static <T> String or(Collection<? extends T> elements, BiConsumer<? super T, StringBuilder> consumer) {
        return conjunction(elements, consumer, "or");
    }
    
    public static <T> String or(T[] elements, BiConsumer<? super T, StringBuilder> consumer) {
        return conjunction(elements, consumer, "or");
    }
    
    
    public static <T> String conjunction(Collection<? extends T> elements, BiConsumer<? super T, StringBuilder> consumer, String conjunction) {
        var builder = new StringBuilder();
        var i = 0;
        for (var element : elements) {
            consumer.accept(element, builder);
            if (i < elements.size() - 2) {
                builder.append(", ");
                
            } else if (i < elements.size() - 1) {
                builder.append(' ').append(conjunction).append(' ');
            }
            i++;
        }
        
        
        return builder.toString();
    }
    
    public static <T> String conjunction(T[] elements, BiConsumer<? super T, StringBuilder> consumer, String conjunction) {
        var builder = new StringBuilder();
        for (int i = 0; i < elements.length; i++) {
            consumer.accept(elements[i], builder);
            if (i < elements.length - 2) {
                builder.append(", ");
                
            } else if (i < elements.length - 1) {
                builder.append(' ').append(conjunction).append(' ');
            }
        }
        
        return builder.toString();
    }
    
    
    public static <T> String join(Collection<? extends T> elements, BiConsumer<? super T, StringBuilder> consumer, String separator) {
        var builder = new StringBuilder();
        var i = 0;
        for (var element : elements) {
            consumer.accept(element, builder);
            if (i < elements.size() - 1) {
                builder.append(separator);
            }
            i++;
        }
        
        return builder.toString();
    }
    
    public static <T> String join(T[] elements, BiConsumer<? super T, StringBuilder> consumer, String separator) {
        var builder = new StringBuilder();
        for (int i = 0; i < elements.length; i++) {
            consumer.accept(elements[i], builder);
            if (i < elements.length - 1) {
                builder.append(separator);
            }
        }
        
        return builder.toString();
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
    
    
    public static String method(String actual, String expected) {
        return "Method is: " + System.lineSeparator()
             + indent(actual, 4) + System.lineSeparator()
             + System.lineSeparator()
             + "should be: " + System.lineSeparator()
             + indent(expected, 4);
    }
    
    
    public static String indent(String message) {
        return indent(message, 4);
    }
    
    public static String indent(String message, int level) {
        var indentation = " ".repeat(level);
        return indentation + message.replace(System.lineSeparator(), System.lineSeparator() + indentation);
    }
    
    
    public static String quote(Object value) {
        return "\"" + value + "\"";
    }
    
}
