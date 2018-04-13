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
package com.karuslabs.commons.command.annotation.processors;

import com.karuslabs.annotations.processors.AnnotationProcessor;
import com.karuslabs.commons.command.annotation.*;

import java.util.*;
import javax.annotation.processing.*;
import javax.lang.model.element.*;

import static javax.tools.Diagnostic.Kind.ERROR;


@SupportedAnnotationTypes({"com.karuslabs.commons.command.annotation.Namespace", "com.karuslabs.commons.command.annotation.Namespaces"})
public class NamespaceProcessor extends AnnotationProcessor {
    
    Map<String, String> namespaces;
    
    
    public NamespaceProcessor() {
        namespaces = new HashMap<>();
    }
    
    
    @Override
    protected void process(Element element) {
        for (Namespace namespace : element.getAnnotationsByType(Namespace.class)) {
            check(element, namespace.value());
        }
    }
    
    protected void check(Element element, String[] namespace) {
        if (namespace.length == 0) {
            messager.printMessage(ERROR, "Invalid namespace, namespace cannot be empty", element);
            return;
        }
        
        String name = String.join(".", namespace);
        String type = element.asType().toString();
        
        String other = namespaces.get(name);
        if (other == null) {
            namespaces.put(name, type);

        } else {
            messager.printMessage(ERROR, "Conflicting namespaces: " + type + " and " + other + ", namespaces must be unique", element);
        }
    }

}
