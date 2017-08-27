/*
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
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
package com.karuslabs.commons.command.arguments;

import java.util.function.*;
import javax.annotation.Nullable;


public class Argument {
    
    public static final Argument NONE = new Argument("");
    
    
    private String argument;
    
    
    public Argument(String argument) {
        this.argument = argument;
    }
    
    
    public boolean match(Predicate<String> match) {
        return match.test(argument);
    }
    
    
    public String raw() {
        return argument;
    }
    
    
    public @Nullable <T> T as(Function<String, T> type) {
        return type.apply(argument);
    }
    
    public <T> T asOrDefault(Function<String, T> type, T value) {
        T parsed = type.apply(argument);
        if (parsed != null) {
            return parsed;
            
        } else {
            return value;
        }
    }
    
    public <T, E extends RuntimeException> T asOrThrow(Function<String, T> type, Supplier<E> exception) {
        T parsed = type.apply(argument);
        if (parsed != null) {
            return parsed;
            
        } else {
            throw exception.get();
        }
    }
    
    
    public void set(String argument) {
        this.argument = argument;
    }
    
}
