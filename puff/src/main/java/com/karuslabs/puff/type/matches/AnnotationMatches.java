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
package com.karuslabs.puff.type.matches;

import com.karuslabs.puff.Format;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.function.Function;
import javax.lang.model.element.Element;
import javax.lang.model.util.Types;

import org.checkerframework.checker.nullness.qual.Nullable;

abstract class AnnotationMatch extends Many<Class<? extends Annotation>> {
    
    static final Function<Class<? extends Annotation>, String> FORMAT = type -> "@" + type.getSimpleName();
    
    static String map(Element element) {
        return null; // TODO
    }
    
    private final String expected;
    
    AnnotationMatch(String expected, Class<? extends Annotation>... annotations) {
        super(annotations);
        this.expected = expected;
    }
    
    @Override
    public String expected() {
        return expected;
    }
    
}

class ExactlyAnnotation extends AnnotationMatch {

    ExactlyAnnotation(Class<? extends Annotation>... annotations) {
        super(Format.and(List.of(annotations), FORMAT), annotations);
    }
    
    @Override
    public @Nullable String match(Element element, Types types) {
        for (var annotation : elements) {
            if (element.getAnnotationsByType(annotation).length == 0) {
                return map(element);
            }
        }
        return null;
    }
    
}
