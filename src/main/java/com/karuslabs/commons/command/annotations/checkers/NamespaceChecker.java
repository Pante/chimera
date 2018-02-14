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
package com.karuslabs.commons.command.annotations.checkers;

import com.karuslabs.commons.command.annotations.*;

import java.util.*;
import javax.annotation.processing.*;
import javax.lang.model.element.*;

import static javax.tools.Diagnostic.Kind.ERROR;


@SupportedAnnotationTypes({"com.karuslabs.commons.command.annotations.Namespace", "com.karuslabs.commons.command.annotations.Namespaces"})
public class NamespaceChecker extends AbstractProcessor {
    
    private Map<String, String> namespaces;
    
    
    public NamespaceChecker() {
        namespaces = new HashMap<>();
    }
    
    
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment enviroment) {
        Set<? extends Element> elements = enviroment.getElementsAnnotatedWith(Namespace.class);
        elements.forEach(element -> check(element, element.getAnnotation(Namespace.class)));
        
        elements = enviroment.getElementsAnnotatedWith(Namespaces.class);
        elements.forEach(element -> {
            for (Namespace namespace : element.getAnnotation(Namespaces.class).value()) {
                check(element, namespace);
            }
        });
        
        return false;
    }
    
    protected void check(Element element, Namespace namespace) {
        String name = String.join(" ", namespace.value());
        String type = element.asType().toString();
        
        String other = namespaces.get(name);
        if (other == null) {
            namespaces.put(name, type);

        } else {
            processingEnv.getMessager().printMessage(ERROR, "Conflicting namespaces: " + type + " and " + other + ", namespaces must be unique", element);
        }
    }

}
