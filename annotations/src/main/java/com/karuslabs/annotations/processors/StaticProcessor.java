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
package com.karuslabs.annotations.processors;

import java.util.Set;
import javax.annotation.processing.*;
import javax.lang.model.element.*;

import static com.karuslabs.annotations.processors.Processors.annotated;
import static javax.lang.model.element.ElementKind.*;
import static javax.lang.model.element.Modifier.*;
import static javax.tools.Diagnostic.Kind.ERROR;


@SupportedAnnotationTypes({
    "com.karuslabs.commons.annotation.Static"
})
public class StaticProcessor extends AbstractProcessor {
    
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment environment) {
        for (Element element : annotated(annotations, environment)) {
            process(element);
        }
        
        return false;
    }
    
    protected void process(Element element) {
        for (Element member : element.getEnclosedElements()) {
            checkConstructor(member);
            checkMembers(member);
        }
    }
    
    protected void checkConstructor(Element member) {
        if (member.getKind() == CONSTRUCTOR && member.getModifiers().contains(PRIVATE)) {
            processingEnv.getMessager().printMessage(ERROR, "Invalid constructor modifier, constructor must either be private or undeclared", member);
        }
    }
    
    protected void checkMembers(Element member) {
        if (!member.getModifiers().contains(STATIC)) {
            processingEnv.getMessager().printMessage(ERROR, "Invalid class member, member must be declared static", member);
        }
    }
    
}
