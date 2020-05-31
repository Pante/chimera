/*
 * The MIT License
 *
 * Copyright 2019 Karus Labs.
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
package com.karuslabs.annotations.processor;

import com.karuslabs.annotations.VisibleForOverride;

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
     * Provides utility methods for manipulating {@code Element}s.
     */
    protected Elements elements;
    /**
     * Provides utility methods for manipulating {@code TypeMirror}s.
     */
    protected Types types;
    /**
     * Used to report errors, warnings and other notes.
     */
    protected Messager messager;
    
    
    /**
     * Initializes this processor and its fields with the processing environment.
     * 
     * @param environment the environment
     */
    @Override
    public void init(ProcessingEnvironment environment) {
        super.init(environment);
        elements = environment.getElementUtils();
        types = environment.getTypeUtils();
        messager = environment.getMessager();
    }
    
    
    /**
     * Processes all elements provided in {@code round} annotated with the
     * given annotations.
     * <br><br>
     * <b>Default implementation:</b><br>
     * Forwards the processing of each annotated element to {@link #process(Element)}.
     * 
     * @param annotations the annotations types requested to be processed
     * @param round environment for information about the current and prior round 
     * @return {@code false}
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment round) {
        for (var element : round.getElementsAnnotatedWithAny(annotations.toArray(new TypeElement[0]))) {
            process(element);
        }
        
        clear();
        
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
    @VisibleForOverride
    protected void process(Element element) {
        
    }
    
    /**
     * Clears this processor after each round of annotation processing.
     * <br><br>
     * <b>Default implementation:</b><br>
     * Does nothing.
     */
    @VisibleForOverride
    protected void clear() {
        
    }
    
    
    /**
     * Emits an error message.
     * 
     * @param message the error message
     */
    public void error(String message) {
        messager.printMessage(ERROR, message);
    }
    
    /**
     * Emits an error message at the location of the given element.
     * 
     * @param element the element
     * @param message the error message
     */
    public void error(Element element, String message) {
        messager.printMessage(ERROR, message, element);
    }
    
    
    /**
     * Emits a warning.
     * 
     * @param message the warning message
     */
    public void warn(String message) {
        messager.printMessage(WARNING, message);
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
     * @param message the note
    */
    public void note(String message) {
         messager.printMessage(NOTE, message);
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
