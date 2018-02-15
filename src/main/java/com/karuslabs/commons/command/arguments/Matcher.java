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

import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Arrays.copyOfRange;


public class Matcher {
    
    private String[] arguments;
    private int first, last;
    
    
    public Matcher(String... arguments) {
        this.arguments = arguments;
        first = 0;
        last = arguments.length;
    }
  
    
    public Matcher all() {
        first = 0;
        last = arguments.length;
        return this;
    }
    
    
    public Matcher single(int index) {
        return between(index, index + 1);
    }
    
    public Matcher starting(int first) {
        return between(first, arguments.length);
    }

    public Matcher between(int first, int last) {
        if (0 <= first && first <= last && last <= arguments.length) {
            this.first = first;
            this.last = last;
            return this;
            
        } else {
            throw new IllegalArgumentException("Invalid bounds specified: " + first + ", " + last);
        }
    }

    
    public Stream<String> stream() {
        return Stream.of(copyOfRange(arguments, first, last));
    }
    
    public boolean exact(Predicate<String>... matches) {
        if (matches.length != length()) {
            return false;
        }
        
        for (int i = 0, j = first; j < last; i++, j++) {
            if (!matches[i].test(arguments[j])) {
                return false;
            }
        }

        return true;
    }
    
    public boolean any(Predicate<String>... matches) {
        if (matches.length > length()) {
            return false;
        }
        
        int i = 0;
        for (String argument : arguments) {
            if (matches[i].test(argument)) {
                i++;
                if (matches.length <= i) {
                    return true;
                }

            } else {
                i = 0;
            }
        }

        return false;
    }
    
    
    public boolean using(Predicate<String[]> matcher) {
        return matcher.test(copyOfRange(arguments, first, last));
    }
    
    
    public int length() {
        return last - first;
    }
    
    
    protected void set(String[] arguments) {
        this.arguments = arguments;
        all();
    }
    
    protected int first() {
        return first;
    }
    
    protected int last() {
        return last;
    }
    
}
