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
package com.karuslabs.commons.locale.annotation.processors;

import com.karuslabs.commons.locale.annotation.*;

import java.util.Set;
import javax.annotation.processing.*;
import javax.lang.model.element.*;

import static com.karuslabs.annotations.processors.Processors.annotated;
import static javax.tools.Diagnostic.Kind.ERROR;


@SupportedAnnotationTypes({
    "com.karuslabs.commons.locale.annotation.Bundle",
    "com.karuslabs.commons.locale.annotation.EmbeddedResources", "com.karuslabs.commons.locale.annotation.ExternalResources"
})
public class ResourceChecker extends AbstractProcessor {
    
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment environment) {
        for (Element element : annotated(annotations, environment)) {
            if (element.getAnnotation(Bundle.class) == null) {
                processingEnv.getMessager().printMessage(ERROR, "Missing bundle name for: " + element.asType().toString(), element);
            }
            
            if (element.getAnnotation(EmbeddedResources.class) != null) {
                check(element, element.getAnnotation(EmbeddedResources.class).value());
            }
            
            if (element.getAnnotation(ExternalResources.class) != null) {
                check(element, element.getAnnotation(ExternalResources.class).value());
            }
        }

        return false;
    }
    
    protected void check(Element element, String[] values) {
        if (values.length > 1) {
            for (int i = 0; i < values.length; i++) {
                for (int j = i + 1; j < values.length; j++) {           
                    String first = values[i];
                    String second = values[j];
                    if (i != j && first != null && first.equals(second)) {
                        processingEnv.getMessager().printMessage(ERROR, "Duplicate resources: " + first + ", resources must be unique", element);
                    }
                }
            }
            
        } else if (values.length == 0) {
            processingEnv.getMessager().printMessage(ERROR, "Resources is empty, resources cannot be empty", element);
        }
        
        
    }
    
}
