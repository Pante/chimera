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


/**
 * A extractor that extracts annotations from a type.
 * 
 * @param <T> the annotated type
 */
public interface Extractor<T> {
    
    /**
     * An extractor that extracts annotations from a {@code Class}.
     */
    public static final Extractor<Class<?>> CLASS = new ClassExtractor();
    /**
     * An extractor that extracts annotations from a {@link Element}.
     */
    public static final Extractor<Element> ELEMENT = new ElementExtractor();
    
    
    /**
     * Extracts all annotations that match {@code annotation} from the {@code type}.
     * 
     * @param <A> the annotation type
     * @param type the annotated type
     * @param annotation the annotation type
     * @return all annotations that match {@code annotation} on {@code type} if present; otherwise an empty array
     */
    public <A extends Annotation> A[] all(T type, Class<A> annotation);
    
    /**
     * Extracts an annotation that matches {@code annotation} from {@code type}.
     * 
     * @param <A> the annotation type
     * @param type the annotated type
     * @param annotation the annotation type
     * @return an annotation that matches {@code annotation} from {@code type} if present; otherwise {@code null}
     */
    public <A extends Annotation> @Nullable A single(T type, Class<A> annotation);
    
}


class ClassExtractor implements Extractor<Class<?>> {

    @Override
    public <A extends Annotation> A[] all(Class<?> type, Class<A> annotation) {
        return type.getAnnotationsByType(annotation);
    }

    @Override
    public <A extends Annotation> A single(Class<?> type, Class<A> annotation) {
        return type.getAnnotation(annotation);
    }
    
}

class ElementExtractor implements Extractor<Element> {

    @Override
    public <A extends Annotation> A[] all(Element element, Class<A> annotation) {
        return element.getAnnotationsByType(annotation);
    }

    @Override
    public <A extends Annotation> A single(Element element, Class<A> annotation) {
        return element.getAnnotation(annotation);
    }
    
}
