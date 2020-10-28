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

import java.util.*;
import javax.lang.model.type.*;
import javax.lang.model.util.Types;

import org.checkerframework.checker.nullness.qual.Nullable;

public class TypeMap<T> {

    private final Types types;
    private final EnumMap<TypeKind, T> primitives;
    private final Map<TypeMirror, T> declared;
    
    public TypeMap(Types types) {
        this(types, new EnumMap<TypeKind, T>(TypeKind.class), new HashMap<>());
    }
    
    public TypeMap(Types types, EnumMap<TypeKind, T> primitives, Map<TypeMirror, T> declared) {
        this.types = types;
        this.primitives = primitives;
        this.declared = declared;
    }
    
    
    public @Nullable T exactly(TypeMirror type) {
        var mapped = primitives.get(type.getKind());
        if (mapped == null) {
            for (var entry : declared.entrySet()) {
                if (types.isSameType(entry.getKey(), type)) {
                    return entry.getValue();
                }
            }
        }
        return mapped;
    }
    
    public @Nullable T subtype(TypeMirror type) {
        var mapped = primitives.get(type.getKind());
        if (mapped == null) {
            for (var entry : declared.entrySet()) {
                if (types.isAssignable(type, entry.getKey())) {
                    return entry.getValue();
                }
            }
        }
        return mapped;
    }
    
    public @Nullable T supertype(TypeMirror type) {
        var mapped = primitives.get(type.getKind());
        if (mapped == null) {
            for (var entry : declared.entrySet()) {
                if (types.isAssignable(entry.getKey(), type)) {
                    return entry.getValue();
                }
            }
        }
        return mapped;
    }
    
    
    public TypeMap<T> put(TypeKind kind, T value) {
        primitives.put(kind, value);
        return this;
    }
    
    public TypeMap<T> put(TypeMirror type, T value) {
        declared.put(type, value);
        return this;
    }
    
}
