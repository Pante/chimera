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

import com.karuslabs.puff.Format;
import com.karuslabs.puff.type.TypeMirrors;

import java.util.*;
import java.util.function.BiConsumer;
import javax.lang.model.element.*;

import org.checkerframework.checker.nullness.qual.Nullable;

abstract class ModifierMatch extends Many<Modifier> {

    static final BiConsumer<Modifier, StringBuilder> FORMAT = (modifier, builder) -> builder.append(modifier.toString().toLowerCase());
    
    private final String expected;
    
    ModifierMatch(String expected, Modifier... modifiers) {
        super(modifiers);
        this.expected = expected;
    }
    
    @Override
    public String condition() {
        return expected;
    }
    
}

class ExactlyModifiers extends ModifierMatch {

    ExactlyModifiers(Modifier... modifiers) {
        super("exactly " + Format.and(List.of(modifiers), FORMAT));
    }
    
    @Override
    public @Nullable String match(Element element, TypeMirrors types) {
        if (element.getModifiers().equals(values)) {
            return null;
        }
        
        return Format.and(element.getModifiers(), FORMAT);
    }
    
}

class ContainsModifiers extends ModifierMatch {

    ContainsModifiers(Modifier... modifiers) {
        super(Format.and(List.of(modifiers), FORMAT), modifiers);
    }
    
    @Override
    public @Nullable String match(Element element, TypeMirrors types) {
        if (element.getModifiers().containsAll(values)) {
            return null;
        }
        
        return Format.and(element.getModifiers(), FORMAT);
    }
    
}

class NoModifiers extends ModifierMatch {

    NoModifiers(Modifier... modifiers) {
        super("neither " + Format.or(List.of(modifiers), FORMAT), modifiers);
    }
    
    @Override
    public @Nullable String match(Element element, TypeMirrors types) {
        if (Collections.disjoint(element.getModifiers(), values)) {
            return null;
        }
        
        return Format.and(element.getModifiers(), FORMAT);
    }
    
}
