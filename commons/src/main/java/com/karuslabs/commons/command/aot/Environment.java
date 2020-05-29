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
package com.karuslabs.commons.command.aot;

import java.util.*;
import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.util.*;

import static javax.tools.Diagnostic.Kind.*;


/**
 * An environment which contains the ASTs for elements and utilities for reporting
 * errors and warning.
 */
public class Environment {

    /**
     * The messager used to report errors and warnings.
     */
    public final Messager messager;
    /**
     * A filer used to create the generated source file.
     */
    public final Filer filer;
    /**
     * Utilities for manipulating elements.
     */
    public final Elements elements;
    /**
     * Utilities for manipulating {@code TypeMirror}s.
     */
    public final Types types;
    /**
     * The ASTs for elements.
     */
    public final Map<Element, Token> scopes;
    boolean error;
    
    
    /**
     * Creates an {@code Environment} with the given parameters.
     * 
     * @param messager the messager
     * @param filer the filer
     * @param elements the elements
     * @param types the type
     */
    public Environment(Messager messager, Filer filer, Elements elements, Types types) {
        this.messager = messager;
        this.filer = filer;
        this.elements = elements;
        this.types = types;
        scopes = new HashMap<>();
        error = false;
    }
    
    
    /**
     * Returns the AST for the given element.
     * 
     * @param element the element
     * @return an AST
     */
    public Token scope(Element element) {
        var existing = scopes.get(element);
        if (existing == null) {
            scopes.put(element, existing = Token.root());
        }
        
        return existing;
    }
    
    
    /**
     * Resets this environment to its original state.
     */
    public void clear() {
        scopes.clear();
        error = false;
    }

    
    /**
     * Emits a warning at the location of the given element.
     * 
     * @param element the element
     * @param message the warning
     */
    public void error(Element element, String message) {
        messager.printMessage(ERROR, message, element);
        error = true;
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
     * Returns whether an error has occurred.
     * 
     * @return {@code true} if an error has bene encounter
     */
    public boolean error() {
        return error;
    }
    
}
