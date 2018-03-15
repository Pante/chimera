/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
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
package com.karuslabs.commons.annotation.checkers;

import java.util.*;
import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.type.TypeKind;

import static com.karuslabs.commons.annotation.checkers.Checkers.*;
import static java.util.Collections.EMPTY_LIST;
import static java.util.stream.Collectors.toList;
import static javax.lang.model.element.Modifier.*;
import static javax.lang.model.type.TypeKind.*;
import static javax.tools.Diagnostic.Kind.ERROR;


@SupportedAnnotationTypes({
    "com.karuslabs.commons.annotation.ValueBased"
})
public class ValueBasedChecker extends AbstractProcessor {
    
    protected List<ExecutableElement> methods;
    
    
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment environment) {
        for (Element element : annotated(annotations, environment)) {
            process(element);
        }
        
        return false;
    }
    
    protected void process(Element element) {
        methods = ElementFilter.methodsIn(element.getEnclosedElements()).stream().filter(method -> !method.getModifiers().contains(STATIC)).collect(toList());
        
        checkClass(element);
        checkEquals(element);
        
        methods = EMPTY_LIST;
    }
    
    protected void checkClass(Element element) {
        if (!element.getModifiers().contains(FINAL)) {
            processingEnv.getMessager().printMessage(ERROR, "Invalid class declaration, class must be declared final", element);
        }
    }
    
    protected void checkEquals(Element element) {
//        check(element, "equals", "boolean equals(Object)", method -> 
//            method.getReturnType().getKind() == BOOLEAN && method.getParameters().size() == 1 && method.getParameters().get(0)..getSimpleName().contentEquals("Object")
//        );
    }
    
    protected void checkHashCode(Element element) {
        check(element, "int hashCode()", "hashCode", INT);
    }
    
    protected void checkToString(Element element) {
//        check(element, "string toString()", "toString", STRING);
    }
    
    protected void check(Element element, String missing, String name, TypeKind value, String... parameters) {
//        for (ExecutableElement method : methods) {
//            if (expect(method, name, BYTE, parameters)) {
//                return;
//            }
//        }
        
        processingEnv.getMessager().printMessage(ERROR, "Missing method: " + missing + ", class must override method", element);
    }
    
}
