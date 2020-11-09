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

import com.karuslabs.puff.Format;
import com.karuslabs.puff.type.*;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.function.BiConsumer;
import javax.lang.model.element.Element;

import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class Annotations extends Many<Class<? extends Annotation>> {
    
    static final BiConsumer<Class<? extends Annotation>, StringBuilder> FORMAT = (type, builder) -> builder.append("@").append(type.getSimpleName());
    
    private final String condition;
    
    Annotations(String condition, Class<? extends Annotation>... annotations) {
        super(annotations);
        this.condition = condition;
    }
    
    @Override
    public String actual(Element element) {
        var annotations = element.getAnnotationMirrors();
        if (annotations.isEmpty()) {
            return "no annotations";
        }
        
        return Format.and(annotations, (annotation, builder) -> annotation.getAnnotationType().accept(TypePrinter.SIMPLE, builder.append('@')));
    }
    
    @Override
    public String condition() {
        return condition;
    }
    
    @Override
    public String singular() {
        return singular + " annotated with " + condition;
    }
    
    @Override
    public String plural() {
        return plural + " annotated with " + condition;
    }
    
}

class ContainsAnnotations extends Annotations {

    ContainsAnnotations(Class<? extends Annotation>... annotations) {
        super(Format.and(List.of(annotations), FORMAT), annotations);
    }
    
    @Override
    public @Nullable String match(Element element, TypeMirrors types) {
        for (var annotation : values) {
            if (element.getAnnotationsByType(annotation).length == 0) {
                return actual(element);
            }
        }
        return null;
    }
    
}

class NoAnnotations extends Annotations {
    
    NoAnnotations(Class<? extends Annotation>... annotations) {
        super("neither " + Format.or(List.of(annotations), FORMAT), annotations);
    }

    @Override
    public @Nullable String match(Element element, TypeMirrors types) {
        for (var annotation : values) {
            if (element.getAnnotationsByType(annotation).length != 0) {
                return actual(element);
            }
        }
        return null;
    }
    
}
