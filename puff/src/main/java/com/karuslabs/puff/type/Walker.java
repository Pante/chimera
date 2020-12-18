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
package com.karuslabs.puff.type;

import com.karuslabs.annotations.Ignored;

import javax.lang.model.type.*;
import javax.lang.model.util.SimpleTypeVisitor9;

import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class Walker<T> extends SimpleTypeVisitor9<TypeMirror, T> {

    public static Walker<TypeMirror> ancestor(TypeMirrors types) {
        return new AncestorWalker(types);
    }
    
    protected final TypeMirrors types;
    
    public Walker(TypeMirrors types) {
        this.types = types;
    }
    
    @Override
    public @Nullable TypeMirror defaultAction(TypeMirror type, @Ignored T parameter) {
        return null;
    }
    
}

class AncestorWalker extends Walker<TypeMirror> {
    
    private final TypeMirror object;
    
    AncestorWalker(TypeMirrors types) {
        super(types);
        object = types.type(Object.class);
    }
    
    @Override
    public @Nullable TypeMirror visitDeclared(DeclaredType type, TypeMirror ancestor) {
        if (types.isSameType(type, ancestor)) {
            return type;
        }
        
        if (types.isSameType(type, object)) {
            return null;
        }
        
        for (var parent : types.directSupertypes(type)) {
            var match = parent.accept(this, ancestor);
            if (match != null) {
                return match;
            }
        }
        
        return null;
    }
    
}
