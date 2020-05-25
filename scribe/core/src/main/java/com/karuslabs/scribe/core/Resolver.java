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
package com.karuslabs.scribe.core;

import java.lang.annotation.Annotation;
import javax.lang.model.element.Element;

import org.checkerframework.checker.nullness.qual.Nullable;


public interface Resolver<T> {
    
    public static final Resolver<Class<?>> CLASS = new ClassResolver();
    public static final Resolver<Element> ELEMENT = new ElmeentResolver();
    
    
    public <A extends Annotation> A[] all(T type, Class<A> annotation);
    
    public <A extends Annotation> @Nullable A any(T type, Class<A> annotation);
    
}


class ClassResolver implements Resolver<Class<?>> {

    @Override
    public <A extends Annotation> A[] all(Class<?> type, Class<A> annotation) {
        return type.getAnnotationsByType(annotation);
    }

    @Override
    public <A extends Annotation> A any(Class<?> type, Class<A> annotation) {
        return type.getAnnotation(annotation);
    }
    
}

class ElmeentResolver implements Resolver<Element> {

    @Override
    public <A extends Annotation> A[] all(Element element, Class<A> annotation) {
        return element.getAnnotationsByType(annotation);
    }

    @Override
    public <A extends Annotation> A any(Element element, Class<A> annotation) {
        return element.getAnnotation(annotation);
    }
    
}
