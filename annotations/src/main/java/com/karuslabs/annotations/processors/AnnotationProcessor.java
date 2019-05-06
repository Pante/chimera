/*
 * The MIT License
 *
 * Copyright 2019 Matthias.
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
import javax.lang.model.util.*;

import static javax.tools.Diagnostic.Kind.*;


/**
 * A skeletal {@code Processor} implementation to minimize the effort required to
 * implement it.
 */
public abstract class AnnotationProcessor extends AbstractProcessor {
    
    /**
     * An empty array.
     */
    protected static final TypeElement[] ARRAY = new TypeElement[0];
    
    
    /**
     * Provides utility methods for operating on elements.
     */
    protected Elements elements;
    /**
     * The messager used to report errors, warnings and other notices.
     */
    protected Messager messager;
    /**
     * Provides utility methods for operating on types.
     */
    protected Types types;
    
    
    /**
     * Initializes this processor and its fields with the processing environment.
     * 
     * @param environment environment for facilities the tool framework provides 
     *                    to the processor 
     */
    @Override
    public synchronized void init(ProcessingEnvironment environment) {
        super.init(environment);
        elements = environment.getElementUtils();
        messager = environment.getMessager();
        types = environment.getTypeUtils();
    }
    
    
    /**
     * Processes all elements provided in the given environment annotated with the
     * given annotations.
     * <br><br>
     * <b>Default implementation:</b><br>
     * Forwards the processing of each annotated element to {@link #process(Element)}.
     * 
     * @param annotations the annotations types requested to be processed
     * @param environment environment for information about the current and prior round 
     * @return {@code false}
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment environment) {
        for (var element : environment.getElementsAnnotatedWithAny(annotations.toArray(ARRAY))) {
            process(element);
        }
        
        return false;
    }
    
    /**
     * Processes the given element.
     * <br><br>
     * <b>Default implementation:</b><br>
     * Does nothing.
     * 
     * @param element the element
     */
    protected void process(Element element) {
        
    }
    
    
    /**
     * Emits a error message at the location of the given element.
     * 
     * @param element the element
     * @param message the error message
     */
    public void error(Element element, String message) {
        messager.printMessage(ERROR, message, element);
    }
    
    /**
     * Emits a warning at the location of the given element.
     * 
     * @param element the element
     * @param message the warning
     */
    public void warn(Element element, String message) {
        messager.printMessage(WARNING, message, element);
    }
    
    /**
     * Emits a note at the location of the given element.
     * 
     * @param element the element
     * @param message the note
     */
    public void note(Element element, String message) {
        messager.printMessage(NOTE, message, element);
    }
    
}
