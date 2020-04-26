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
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.util.*;

import org.checkerframework.checker.nullness.qual.Nullable;

import static javax.tools.Diagnostic.Kind.*;


public class Environment {
    
    public final Messager messager;
    public final Elements elements;
    public final Types types;
    public final Map<Element, IR> scopes;
    @Nullable Element element;
    boolean error;
    
    
    public Environment(Messager messager, Elements elements, Types types) {
        this.messager = messager;
        this.elements = elements;
        this.types = types;
        scopes = new HashMap<>();
        error = false;
    }
    
    
    public void initialize(Element element) {
        this.element = element;
    }
    
    
    public IR scope(Element element) {
        var existing = scopes.get(element);
        if (existing == null) {
            existing = IR.root();
            scopes.put(element, existing);
        }
        
        return existing;
    }
    
    
    public void error(String message) {
        error(message, element);
    }
    
    public void error(String message, Element element) {
        messager.printMessage(ERROR, message, element);
        error = true;
    }
    

    public void warn(String message) {
        warn(message, element);
    }
    
    public void warn(String message, Element element) {
        messager.printMessage(WARNING, message, element);
        error = true;
    }
    
    
    public boolean error() {
        return error;
    }
    
}
