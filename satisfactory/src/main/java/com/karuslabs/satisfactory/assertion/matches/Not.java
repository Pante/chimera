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
package com.karuslabs.Satisfactory.assertion.matches;

import com.karuslabs.Satisfactory.assertion.SkeletonAssertion;
import com.karuslabs.Satisfactory.type.TypeMirrors;

import javax.lang.model.element.Element;

public final class Not<T> extends SkeletonAssertion implements Timeable<T> {

    public static <T> Timeable<T> of(Timeable<T> match) {
        return new Not<>(match);
    }
    
    public static <T> Match<T> of(Match<T> match) {
        return new Not<>(match);
    }
    
    
    private final Match<T> match;
    
    private Not(Match<T> match) {
        super("not " + match.condition(), "not " + match.conditions());
        this.match = match;
    }
    
    
    @Override
    public boolean test(TypeMirrors types, Element element) {
        return !match.test(types, element);
    }

    @Override
    public boolean test(TypeMirrors types, T value) {
        return !match.test(types, value);
    }

    @Override
    public String describe(Element element) {
        return match.describe(element);
    }

    @Override
    public String describe(T value) {
        return match.describe(value);
    }

}
