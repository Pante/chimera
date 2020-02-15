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
package com.karuslabs.scribe.core;

import com.karuslabs.annotations.ValueType;

import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;


public @ValueType class Message<T> {

    public static enum Type {
        INFO, WARNING, ERROR
    }
    
    
    public final @Nullable T location;
    public final String value;
    public final Type type;
    
    
    public static <T> Message<T> error(@Nullable T location, String value) {
        return new Message(location, value, Type.ERROR);
    }
    
    public static <T> Message<T> warning(@Nullable T location, String value) {
        return new Message(location, value, Type.WARNING);
    }
    
    public static <T> Message<T> info(@Nullable T location, String value) {
        return new Message(location, value, Type.INFO);
    }
    
    public Message(@Nullable T location, String value, Type type) {
        this.location = location;
        this.value = value;
        this.type = type;
    }
    
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Message<?>)) {
            return false;
        }
        
        var other = (Message<T>) object;
        return Objects.equals(location, other.location) && value.equals(other.value) && type == other.type;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.location);
        hash = 67 * hash + Objects.hashCode(this.value);
        hash = 67 * hash + Objects.hashCode(this.type);
        return hash;
    }
    
}
