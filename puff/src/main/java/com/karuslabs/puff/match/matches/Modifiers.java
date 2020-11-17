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
package com.karuslabs.puff.match.matches;

import com.karuslabs.puff.Texts;
import com.karuslabs.puff.match.*;
import com.karuslabs.puff.type.TypeMirrors;

import java.util.*;
import java.util.function.BiConsumer;
import javax.lang.model.element.*;

abstract class ModifierMatch extends AbstractDescription implements Match<Modifier> {
    
    static final BiConsumer<Modifier, StringBuilder> FORMAT = (modifier, builder) -> builder.append(modifier.toString().toLowerCase().replace('_', ' '));
    
    final Set<Modifier> modifiers;
    
    ModifierMatch(Set<Modifier> modifiers, String expectation) {
        super(expectation);
        this.modifiers = modifiers;
    }

    @Override
    public String describe(Element element) {
        return Texts.and(element.getModifiers(), FORMAT);
    }
    
    @Override
    public String describe(Modifier modifier) {
        return modifier.toString().toLowerCase().replace('_', ' ');
    }
    
}

class AnyModifier extends ModifierMatch {

    AnyModifier() {
        super(Set.of(), "");
    }

    @Override
    public boolean match(TypeMirrors types, Element element) {
        return true;
    }
    
}

class ExactModifiers extends ModifierMatch {

    ExactModifiers(Modifier... modifiers) {
        super(Set.of(modifiers), "exactly " + Texts.and(modifiers, FORMAT));
    }
    
    @Override
    public boolean match(TypeMirrors types, Element element) {
        return element.getModifiers().equals(modifiers);
    }
    
}

class ContainsModifiers extends ModifierMatch {

    ContainsModifiers(Modifier... modifiers) {
        super(Set.of(modifiers), Texts.and(modifiers, FORMAT));
    }
    
    @Override
    public boolean match(TypeMirrors types, Element element) {
        return element.getModifiers().containsAll(modifiers);
    }
    
}

class NoModifiers extends ModifierMatch {

    NoModifiers(Modifier... modifiers) {
        super(Set.of(modifiers), "neither " + Texts.or(modifiers, FORMAT));
    }
    
    @Override
    public boolean match(TypeMirrors types, Element element) {
        return Collections.disjoint(element.getModifiers(), modifiers);
    }
    
}