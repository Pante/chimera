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

import com.karuslabs.puff.match.*;
import com.karuslabs.puff.match.sequence.Sequence;
import com.karuslabs.puff.type.TypeMirrors;

import java.lang.annotation.Annotation;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;

import static java.util.stream.Collectors.toList;

public class Method extends AbstractDescription {

    private final Match<Modifier> modifiers;
    private final Match<Class<? extends Annotation>> annotations;
    private final Match<TypeMirror> type;
    private final Sequence<Variable> arguments;
    private final Sequence<TypeMirror> thrown;
    
    public Method(Match<Modifier> modifiers, Match<Class<? extends Annotation>> annotations, Match<TypeMirror> type, Sequence<Variable> arguments, Sequence<TypeMirror> thrown, String expectation, String expectations) {
        super(expectation, expectations);
        this.modifiers = modifiers;
        this.annotations = annotations;
        this.type = type;
        this.arguments = arguments;
        this.thrown = thrown;
    }
    
    public boolean match(TypeMirrors types, Element element) {
        if (!(element instanceof ExecutableElement)) {
            return false;
        }
        
        var method = (ExecutableElement) element;
        return modifiers.match(types, method) && annotations.match(types, method) 
            && type.match(types, method) && arguments.match(method.getParameters(), types) 
            && thrown.match(method.getThrownTypes().stream().map(type -> types.asElement(type)).collect(toList()), types);
    }

    public String describe(Element element) {
        return null; // TODO: ?
    }

}
