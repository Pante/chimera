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
package com.karuslabs.puff.type.match.matches;

import com.karuslabs.puff.type.TypeMirrors;
import javax.lang.model.element.Element;
import org.checkerframework.checker.nullness.qual.Nullable;

public class Any<T> implements Match<T> {

    private final String singular;
    private final String plural;
    
    public Any(String singular, String plural) {
        this.singular = singular;
        this.plural = plural;
    }
    
    @Override
    public @Nullable String match(Element element, TypeMirrors types) {
        return null;
    }

    @Override
    public String actual(Element element) {
        return "";
    }

    @Override
    public String condition() {
        return singular;
    }

    @Override
    public String singular() {
        return singular;
    }

    @Override
    public String plural() {
        return plural;
    }

}
