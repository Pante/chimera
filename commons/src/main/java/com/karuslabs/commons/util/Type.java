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
package com.karuslabs.commons.util;

import java.util.*;

import org.checkerframework.checker.nullness.qual.Nullable;


/**
 * Constants of primitive types and their corresponding boxed types.
 */
public enum Type {
    
    BOOLEAN(Boolean.class, boolean.class),
    CHAR(Character.class, char.class),
    STRING(String.class, String.class),
    BYTE(Byte.class, byte.class),
    SHORT(Short.class, short.class),
    INT(Integer.class, int.class),
    LONG(Long.class, long.class),
    FLOAT(Float.class, float.class),
    DOUBLE(Double.class, double.class),
    TYPE(null, null);
    
    
    private static final Map<Class<?>, Type> TYPES;
    
    static {
        TYPES = new HashMap<>();
        register(BOOLEAN);
        register(CHAR);
        register(STRING);
        register(BYTE);
        register(SHORT);
        register(INT);
        register(LONG);
        register(FLOAT);
        register(DOUBLE);
    }
    
    private static void register(Type type) {
        TYPES.put(type.boxed, type);
        TYPES.put(type.unboxed, type);
    }
    
    
    /**
     * Returns the {@code Type} of the {@code object}, or {@link #TYPE} if the
     * {@code object} is neither primitive nor boxed.
     * 
     * @param object the object
     * @return the {@code Type} of the {@code object}, or {@code TYPE} if the object
     *         is neither primitive nor boxed
     */
    public static Type of(Object object) {
        return of(object.getClass());
    }
    
    /**
     * Returns the {@code Type} of the {@code type}, or {@link #TYPE} if the
     * {@code type} is neither primitive nor boxed.
     * 
     * @param type the {@code class}
     * @return the {@code Type} of the {@code class}, or {@code TYPE} if the object
     *         is neither primitive nor boxed
     */
    public static Type of(Class<?> type) {
        return TYPES.getOrDefault(type, TYPE);
    }
    
    
    /**
     * The boxed type.
     */
    public final @Nullable Class<?> boxed;
    /**
     * The primitive type.
     */
    public final @Nullable Class<?> unboxed;


    private Type(Class<?> boxed, Class<?> unboxed) {
        this.boxed = boxed;
        this.unboxed = unboxed;
    }
    
}
