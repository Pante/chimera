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
package com.karuslabs.commons.annotation.checkers.signatures;

import java.util.Set;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;


public abstract class Signature<T extends Element> {
    
    protected String name;
    protected Set<Modifier> modifiers;
    protected TypeMirror type;
    
    
    public Signature(String name, Set<Modifier> modifiers, TypeMirror type) {
        this.modifiers = modifiers;
        this.type = type;
    }
    
            
    public abstract boolean exact(ProcessingEnvironment environment, T element);
    
    
    public boolean hasName(T element) {
        return element.getSimpleName().contentEquals(name);
    }
    
    public boolean hasModifiers(T element) {
        return modifiers.equals(element.getModifiers());
    }
    
    
    public String getName() {
        return name;
    }
    
    public Set<Modifier> getModifiers() {
        return modifiers;
    }
        
    public TypeMirror getType() {
        return type;
    }
    
}
