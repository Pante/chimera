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
package com.karuslabs.puff.type.match.times;

import com.karuslabs.puff.type.TypeMirrors;
import com.karuslabs.puff.type.match.Match;

import javax.lang.model.element.Element;

import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class Times<T> {
    
    public static <T> Times<T> exactly(int times, Match<T> match) {
        return new Exactly<>(times, match);
    }
    
    
    protected final Match<T> match;
    protected int current;
    
    public Times(Match<T> match) {
        this.match = match;
        this.current = 0;
    }
    
    public abstract @Nullable String match();
    
    public void add(Element element, TypeMirrors types) {
        if (match.match(element, types) == null) {
            current++;
        }
    }
    
    public void reset() {
        current = 0;
    }
    
    public abstract String condition();
    
}
