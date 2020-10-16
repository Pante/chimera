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
package com.karuslabs.annotations.processor;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.*;
import javax.lang.model.util.*;

import org.checkerframework.checker.nullness.qual.Nullable;

public final class Typing {
    
    public final Elements elements;
    public final Types types;
    
    public Typing(Elements elements, Types types) {
        this.elements = elements;
        this.types = types;
    }
    
    
    public @Nullable TypeElement element(TypeMirror type) {
        var element = types.asElement(type);
        if (element instanceof TypeElement) {
            return (TypeElement) element;
            
        } else {
            return null;
        }
    }
    
    public TypeMirror box(TypeMirror type) {
        if (type instanceof PrimitiveType) {
            return types.boxedClass((PrimitiveType) type).asType();
            
        } else {
            return type;
        }
    }
    
    
    public TypeMirror type(Class<?> type) {
        return elements.getTypeElement(type.getName()).asType();
    }
    
    public TypeMirror erasure(Class<?> type) {
        return types.erasure(elements.getTypeElement(type.getName()).asType());
    }
    
    public TypeMirror specialize(Class<?> type, Class<?>... parameters) {
        var mirrors = new TypeMirror[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            mirrors[i] = type(parameters[i]);
        }
        
        return specialize(type, mirrors);
    }
    
    public TypeMirror specialize(Class<?> type, TypeMirror... parameters) {
        return types.getDeclaredType(elements.getTypeElement(type.getName()), parameters);
    }
    
}
