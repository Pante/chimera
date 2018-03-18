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
package com.karuslabs.commons.annotation.signature;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;


public class Type {
    
    public static final Type ANY = new Type(null, (TypeMirror) null) {
        
        @Override
        public boolean exact(TypeMirror type) {
            return true;
        }

        @Override
        public boolean from(TypeMirror type) {
            return true;
        }

        @Override
        public boolean to(TypeMirror type) {
            return true;
        }
        
    };
    
    
    private Types types;
    private TypeMirror expected;

    
    public Type(ProcessingEnvironment environment, Class<?> expected) {
        this(environment.getTypeUtils(), environment.getElementUtils().getTypeElement(expected.getCanonicalName()).asType());
    }
    
    public Type(Types types, TypeMirror expected) {
        this.types = types;
        this.expected = expected;
    }
    
    
    public boolean exact(TypeMirror type) {
        return types.isSameType(type, expected);
    }

    public boolean from(TypeMirror type) {
        return types.isAssignable(type, expected);
    }
    
    public boolean to(TypeMirror type) {
        return types.isAssignable(expected, type);
    }
    
}
