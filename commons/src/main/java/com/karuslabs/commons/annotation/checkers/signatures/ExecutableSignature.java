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

import java.util.List;
import java.util.Set;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;


public abstract class ExecutableSignature extends Signature<ExecutableElement> {
    
    protected List<TypeMirror> parameters;
    protected List<TypeMirror> thrown;
    
    
    public ExecutableSignature(String name, Set<Modifier> modifiers, TypeMirror type, List<TypeMirror> parameters, List<TypeMirror> thrown) {
        super(name, modifiers, type);
        this.parameters = parameters;
        this.thrown = thrown;
    }
  
    
    @Override
    public boolean exact(ProcessingEnvironment environment, ExecutableElement element) {
        Types types = environment.getTypeUtils();
        return true;
//        return hasName(element) && hasModifiers(element) && exactTypes(types, element);
    }
    
                    
    public abstract boolean covariant(ExecutableElement element);
    
    
    public List<TypeMirror> getParameters() {
        return parameters;
    }
    
    public List<TypeMirror> getThrown() {
        return thrown;
    }
    
}
