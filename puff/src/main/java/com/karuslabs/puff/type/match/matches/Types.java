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
package com.karuslabs.puff.type.match.matches;

import com.karuslabs.puff.type.*;

import javax.lang.model.element.Element;
import javax.lang.model.type.*;

import org.checkerframework.checker.nullness.qual.Nullable;

abstract class TypeMatch implements Match<TypeMirror> {

    final TypeMirror expected;
    final String condition;
    
    TypeMatch(TypeMirror expected, String condition) {
        this.expected = expected;
        this.condition = condition;
    }
    
    @Override
    public String condition() {
        return condition;
    }
    
    @Override
    public String singular() {
        return condition;
    }
    
}

class ExactlyPrimitive implements Match<TypeMirror> {

    private final TypeKind expected;
    private final String condition;
    
    ExactlyPrimitive(TypeKind expected) {
        this.expected = expected;
        this.condition = expected.toString().toLowerCase();
    }
    
    @Override
    public @Nullable String match(Element element, TypeMirrors types) {
        var kind = element.asType().getKind();
        if (kind != expected) {
            return kind.toString().toLowerCase();
            
        } else {
            return null;
        }
    }

    @Override
    public String condition() {
        return condition;
    }

    @Override
    public String singular() {
        return condition;
    }

    @Override
    public String plural() {
        return condition + "s";
    }
    
}

class ExactlyType extends TypeMatch {
    
    ExactlyType(TypeMirror expected) {
        super(expected, TypePrinter.simple(expected));
    }
    
    @Override
    public @Nullable String match(Element element, TypeMirrors types) {
        var type = element.asType();
        if (!types.isSameType(expected, type)) {
            return type.toString();
            
        } else {
            return null;
        }
    }

    @Override
    public String plural() {
        return condition + "s";
    }
    
}

class Subtype extends TypeMatch {

    private final String plural;
    
    Subtype(TypeMirror expected) {
        this(expected, TypePrinter.simple(expected));
    }
    
    Subtype(TypeMirror expected, String type) {
        super(expected, "subtype of " + type);
        plural = "subtypes of " + type;
    }
    
    @Override
    public @Nullable String match(Element element, TypeMirrors types) {
        var type = element.asType();
        if (!types.isSubtype(type, expected)) {
            return type.toString();
            
        } else {
            return null;
        }
    }

    @Override
    public String plural() {
        return plural;
    }
    
}

class Supertype extends TypeMatch {
    
    private final String plural;
    
    Supertype(TypeMirror expected) {
        this(expected, TypePrinter.simple(expected));
    }
    
    Supertype(TypeMirror expected, String type) {
        super(expected, "supertypes of " + type);
        plural = "supertypes of " + type;
    }
    
    @Override
    public @Nullable String match(Element element, TypeMirrors types) {
        var type = element.asType();
        if (!types.isSubtype(expected, type)) {
            return type.toString();
            
        } else {
            return null;
        }
    }

    @Override
    public String plural() {
        return plural;
    }
    
}