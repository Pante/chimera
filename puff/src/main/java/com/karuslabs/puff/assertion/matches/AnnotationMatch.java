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

import com.karuslabs.puff.Texts;
import com.karuslabs.puff.assertion.SkeletonAssertion;
import com.karuslabs.puff.type.TypeMirrors;
import com.karuslabs.puff.type.TypePrinter;

import java.lang.annotation.Annotation;
import java.util.function.BiConsumer;
import javax.lang.model.element.*;

public abstract class AnnotationMatch extends SkeletonAssertion implements Match<Class<? extends Annotation>> {

    public static final Match<Class<? extends Annotation>> ANY = new AnyAnnotation();
    
    public static Match<Class<? extends Annotation>> contains(Class<? extends Annotation>... annotations) {
        return new ContainsAnnotation(annotations);
    }
    
    public static final BiConsumer<Class<? extends Annotation>, StringBuilder> CLASS_FORMAT = (type, builder) -> builder.append("@").append(type.getSimpleName());
    public static final BiConsumer<AnnotationMirror, StringBuilder> MIRROR_FORMAT = (annotation, builder) -> annotation.getAnnotationType().accept(TypePrinter.SIMPLE, builder.append('@'));
    
    final Class<? extends Annotation>[] annotations;
    
    public AnnotationMatch(Class<? extends Annotation>[] annotations, String condition) {
        super(condition);
        this.annotations = annotations;
    }

    @Override
    public String describe(Element element) {
        var annotations = element.getAnnotationMirrors();
        if (annotations.isEmpty()) {
            return "";
            
        } else {
            return Texts.join(annotations, MIRROR_FORMAT, " ");
        }
    }
 
    @Override
    public String describe(Class<? extends Annotation> annotation) {
        return "@" + annotation.getSimpleName();
    }

}

class AnyAnnotation extends AnnotationMatch {

    AnyAnnotation() {
        super(new Class[0], "");
    }

    @Override
    public boolean test(TypeMirrors types, Element element) {
        return true;
    }

    @Override
    public boolean test(TypeMirrors types, Class<? extends Annotation> value) {
        return true;
    }
    
}

class ContainsAnnotation extends AnnotationMatch {

    ContainsAnnotation(Class<? extends Annotation>[] annotations) {
        super(annotations, Texts.join(annotations, CLASS_FORMAT, " "));
    }

    @Override
    public boolean test(TypeMirrors types, Element element) {
        for (var annotation : annotations) {
            if (element.getAnnotationsByType(annotation).length == 0) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean test(TypeMirrors types, Class<? extends Annotation> value) {
        for (var annotation : annotations) {
            if (annotation.equals(value)) {
                return true;
            }
        }
        
        return false;
    }
    
}