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

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.util.Types;

import static javax.tools.Diagnostic.Kind.*;


public abstract class AnnotationProcessor extends CompilationProcessor<Element> {
    
    protected Messager messager;
    protected Types types;
    
    
    @Override
    public synchronized void init(ProcessingEnvironment environment) {
        super.init(environment);
        messager = environment.getMessager();
        types = environment.getTypeUtils();
        initialise(environment);
    }
    
    
    @Override
    protected Element from(Element element) {
        return element;
    }
    
    
    @Override
    protected void error(Element element, String message) {
        messager.printMessage(ERROR, message, element);
    }
    
    @Override
    protected void warning(Element element, String message) {
        messager.printMessage(WARNING, message, element);
    }
    
    @Override
    protected void note(Element element, String message) {
        messager.printMessage(NOTE, message, element);
    }
    
}
