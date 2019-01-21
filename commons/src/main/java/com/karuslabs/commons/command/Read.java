/*
 * The MIT License
 *
 * Copyright 2019 Karus Labs.
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
package com.karuslabs.commons.command;

import com.karuslabs.annotations.Static;

import com.mojang.brigadier.StringReader;

import java.util.function.Predicate;
import java.util.regex.Pattern;


public @Static class Read {
    
    public static final Pattern COMMA = Pattern.compile("([,]\\s*)");
    
    
    public static String until(StringReader reader, char delimiter) {
        var start = reader.getCursor();
        while (reader.canRead() && reader.peek() != delimiter) {
            reader.skip();
        }
        
        return reader.getString().substring(start, reader.getCursor());
    }
    
    public static String until(StringReader reader, char... delimiters) {
        var start = reader.getCursor();
        while (reader.canRead() && !contains(delimiters, reader.peek())) {
            reader.skip();
        }
        
        return reader.getString().substring(start, reader.getCursor());
    }
    
    private static boolean contains(char[] characters, char value) {
        for (var c : characters) {
            if (value == c) {
                return true;
            }
        }
        
        return false;
    }
    
        
    public static String until(StringReader reader, Predicate<Character> end) {
        var start = reader.getCursor();
        while (reader.canRead() && !end.test(reader.peek())) {
            reader.skip();
        }
        
        return reader.getString().substring(start, reader.getCursor());
    }
    
    
}
