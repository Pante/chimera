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

import com.karuslabs.commons.annotation.*;

import java.util.function.*;

import static java.util.Arrays.copyOfRange;


public class Arguments {
    
    private static final String[] EMPTY = new String[] {};
    
    
    private String[] arguments;
    private Matcher matcher;
    private Argument mutable;
    
    
    public Arguments(String... arguments) {
        this.arguments = arguments;
        mutable = new Argument("");
    }
    
    
    public boolean contains(int index) {
        return 0 <= index && index < arguments.length;
    }
        
    
     public @Shared Matcher match() {        
        if (matcher == null) {
            matcher = new Matcher(arguments);
            
        } else {
            matcher.set(arguments);
        }
        
        return matcher;
    }
    
    
    public @Shared Argument get(int index) {
        mutable.set(contains(index) ? arguments[index] : "");
        return mutable;
    }
    
    public @Shared Argument getLast() {
        return get(arguments.length - 1);
    }
    
    
    public @Immutable Argument getImmutable(int index) {
        return getOr(index, Argument::new, Argument::new);
    }
    
    public @Immutable Argument getLastImmutable() {
        return getOr(arguments.length - 1, Argument::new, Argument::new);
    }
    
    
    protected @Shared Argument getOr(int index, Function<String, Argument> function, Supplier<Argument> supplier) {
        if (contains(index)) {
            return function.apply(arguments[index]);
            
        } else {
            return supplier.get();
        }
    }
    
        
    public int length() {
        return arguments.length;
    }
    
        
    public void trim() {
        if (arguments.length <= 1) {
            arguments = EMPTY;
            
        } else {
            arguments = copyOfRange(arguments, 1, arguments.length);
        }
    }
    
    
    public String[] get() {
        return arguments;
    }
    
    protected void set(String[] arguments) {
        this.arguments = arguments;
    }
    
}
