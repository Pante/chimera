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
package com.karuslabs.commons.command.argument;

import java.util.Arrays;


public class Arguments {
    
    public static final String[] EMPTY = new String[] {};
    
    
    private String[] args;
    
    
    public Arguments() {
        this(EMPTY);
    }
    
    public Arguments(String[] args) {
        this.args = args;
    }
    
    
    public boolean hasLength(int index) {
        return  0 <= index && index < args.length;
    }
    
    
    public String getString(int index) {
        if (0 <= index && index < args.length) {
            return args[index];
            
        } else {
            return "";
        }
    }
    
    public String getStringOrDefault(int index, String value) {
        if (0 <= index && index < args.length) {
            return args[index];
            
        } else {
            return value;
        }
    }
    
    public String getLastString() {
        return getString(args.length - 1);
    }
    
    
    public Argument get(int index) {
        if (0 <= index && index < args.length) {
            return new Argument(args[index]);
            
        } else {
            return Argument.EMPTY;
        }
    }    
    
    public Argument getOrDefault(int index, String value) {
        if (0 <= index && index < args.length) {
            return new Argument(args[index]);
            
        } else {
            return new Argument(value);
        }
    }
    
    public Argument getOrDefault(int index, Argument value) {
        if (0 <= index && index < args.length) {
            return new Argument(args[index]);
            
        } else {
            return value;
        }
    }
        
    public Argument getLast() {
        return get(args.length - 1);
    }
    
    
    public void trim() {
        if (args.length <= 1) {
            args = EMPTY;
            
        } else {
            args = Arrays.copyOfRange(args, 1, args.length);
        }
    }
    
    
    public String[] getRawArguments() {
        return args;
    }
    
    public void setRawArguments(String[] args) {
        this.args = args;
    }
    
}
