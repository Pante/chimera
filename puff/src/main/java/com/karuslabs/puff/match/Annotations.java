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
package com.karuslabs.puff.match;

import com.karuslabs.puff.Texts;
import com.karuslabs.puff.type.*;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.function.BiConsumer;
import javax.lang.model.element.Element;

abstract class AnnotationMatch implements Match<Class<? extends Annotation>> {
    
    public static Match<Class<? extends Annotation>> contains(Class<? extends Annotation>... annotations) {
        return new ContainsAnnotations(annotations);
    }
    
    public static Match<Class<? extends Annotation>> no(Class<? extends Annotation>... annotations) {
        return new NoAnnotations(annotations);
    }
    
    static final BiConsumer<Class<? extends Annotation>, StringBuilder> FORMAT = (type, builder) -> builder.append("@").append(type.getSimpleName());
    
    final Class<? extends Annotation>[] annotations;
    final String expectation;
    
    AnnotationMatch(Class<? extends Annotation>[] annotations, String expectation) {
        this.annotations = annotations;
        this.expectation = expectation;
    }
    
    @Override
    public String describe(Element element) {
        var annotations = element.getAnnotationMirrors();
        if (annotations.isEmpty()) {
            return "no annotation";
        }
        
        return Texts.and(annotations, (annotation, builder) -> annotation.getAnnotationType().accept(TypePrinter.SIMPLE, builder.append('@')));
    }

    @Override
    public String expectation() {
        return expectation;
    }
    
}

class ContainsAnnotations extends AnnotationMatch {

    ContainsAnnotations(Class<? extends Annotation>... annotations) {
        super(annotations, Texts.and(annotations, FORMAT));
    }
    
    @Override
    public boolean match(TypeMirrors types, Element element) {
        for (var annotation : annotations) {
            if (element.getAnnotationsByType(annotation).length == 0) {
                return false;
            }
        }
        
        return true;
    }
    
}

class NoAnnotations extends AnnotationMatch {
    
    NoAnnotations(Class<? extends Annotation>... annotations) {
        super(annotations, "neither " + Texts.or(annotations, FORMAT));
    }

    @Override
    public boolean match(TypeMirrors types, Element element) {
        for (var annotation : annotations) {
            if (element.getAnnotationsByType(annotation).length != 0) {
                return false;
            }
        }
        return true;
    }
    
}
