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
package com.karuslabs.commons.command.annotation.checkers;

import com.karuslabs.commons.command.annotation.Namespace;
import com.karuslabs.commons.command.annotation.Namespaces;
import com.karuslabs.commons.command.CommandExecutor;

import java.util.Set;
import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.lang.model.type.*;

import static com.karuslabs.commons.annotation.checker.Elements.annotated;
import static javax.tools.Diagnostic.Kind.ERROR;


@SupportedAnnotationTypes({
    "com.karuslabs.commons.command.annotation.Information", 
    "com.karuslabs.commons.command.annotation.Literal", "com.karuslabs.commons.command.annotation.Literals",
    "com.karuslabs.commons.command.annotation.Namespace", "com.karuslabs.commons.command.annotation.Namespaces",
    "com.karuslabs.commons.command.annotation.Registered", "com.karuslabs.commons.command.annotation.Registrations"
})
public class CommandChecker extends AbstractProcessor {
    
    private TypeMirror expected;
    private Messager messager;
    
    
    @Override
    public synchronized void init(ProcessingEnvironment enviroment) {
        super.init(enviroment);
        expected = enviroment.getElementUtils().getTypeElement(CommandExecutor.class.getName()).asType();
        messager = enviroment.getMessager();
    }
    
    
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment enviroment) {
        for (Element element : annotated(annotations, enviroment))  {
            checkAssignability(element);
            checkNamespace(element);
        }
        
        return false;
    }
    
    protected void checkAssignability(Element element) {
        if (!processingEnv.getTypeUtils().isAssignable(element.asType(), expected)) {
            messager.printMessage(ERROR, "Invalid annotated type: " + element.asType().toString() + ", type must implement " + CommandExecutor.class.getName() , element);
        }
    }
    
    protected void checkNamespace(Element element) {
        if (element.getAnnotation(Namespace.class) == null && element.getAnnotation(Namespaces.class) == null) {
            messager.printMessage(ERROR, "Missing namespace(s): " + element.asType().toString() + ", command must be declared with a namespace", element);
        }
    }
    
}
