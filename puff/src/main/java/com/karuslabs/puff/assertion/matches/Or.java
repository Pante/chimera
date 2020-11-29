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
package com.karuslabs.puff.assertion.matches;

import com.karuslabs.puff.assertion.SkeletonAssertion;
import com.karuslabs.puff.type.TypeMirrors;

import javax.lang.model.element.Element;

import static com.karuslabs.puff.Texts.join;

public final class Or<T> extends SkeletonAssertion implements Timeable<T> {

    private final Match<T> left;
    private final Match<T> right;
    
    public Or(Match<T> left, Match<T> right) {
        super(join(left.condition(), ", or ", right.condition()), join(left.conditions(), ", or ", right.conditions()));
        this.left = left;
        this.right = right;
    }
    
    @Override
    public boolean test(TypeMirrors types, Element element) {
        return left.test(types, element) || right.test(types, element);
    }

    @Override
    public boolean test(TypeMirrors types, T value) {
        return left.test(types, value) || right.test(types, value);
    }

    @Override
    public String describe(Element value) {
        return left.describe(value);
    }
    
    @Override
    public String describe(T value) {
        return left.describe(value);
    }
    
}
