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
package com.karuslabs.smoke.type.matches;

import javax.lang.model.element.Element;
import javax.lang.model.type.*;
import javax.lang.model.util.Types;

import org.checkerframework.checker.nullness.qual.Nullable;

abstract class TypeMatch implements Match<TypeMirror> {

    final TypeMirror expected;
    
    TypeMatch(TypeMirror expected) {
        this.expected = expected;
    }
    
}

class ExactlyPrimitive implements Match<TypeMirror> {

    private final TypeKind expected;
    
    ExactlyPrimitive(TypeKind expected) {
        this.expected = expected;
    }
    
    @Override
    public @Nullable String match(Element element, Types types) {
        var kind = element.asType().getKind();
        if (kind != expected) {
            return kind.toString().toLowerCase();
            
        } else {
            return null;
        }
    }

    @Override
    public String expected() {
        return expected.toString().toLowerCase();
    }
    
}

class ExactlyType extends TypeMatch {
    
    ExactlyType(TypeMirror expected) {
        super(expected);
    }
    
    @Override
    public @Nullable String match(Element element, Types types) {
        var type = element.asType();
        if (!types.isSameType(expected, type)) {
            return type.toString();
            
        } else {
            return null;
        }
    }

    @Override
    public String expected() {
        return expected.toString();
    }
    
}

class Subtype extends TypeMatch {

    Subtype(TypeMirror type) {
        super(type);
    }
    
    @Override
    public @Nullable String match(Element element, Types types) {
        var type = element.asType();
        if (!types.isSubtype(type, expected)) {
            return type.toString();
            
        } else {
            return null;
        }
    }

    @Override
    public String expected() {
        return "subtype of " + expected.toString();
    }
    
}

class Supertype extends TypeMatch {
    
    Supertype(TypeMirror expected) {
        super(expected);
    }
    
    @Override
    public @Nullable String match(Element element, Types types) {
        var type = element.asType();
        if (!types.isSubtype(expected, type)) {
            return type.toString();
            
        } else {
            return null;
        }
    }
    
    @Override
    public String expected() {
        return "supertype of " + expected.toString();
    }
    
}