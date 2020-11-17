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
import java.util.Collection;
import javax.lang.model.element.*;
import javax.lang.model.type.*;

import org.checkerframework.checker.nullness.qual.Nullable;

import static com.karuslabs.puff.Texts.*;
import static com.karuslabs.puff.match.matches.Matches.*;
import com.karuslabs.puff.type.TypePrinter;
import static java.util.stream.Collectors.toList;

public class Method extends AbstractDescription {

    static String format(String modifiers, String name, String annotations, String type, String arguments, String thrown) {
        var description = join(join(modifiers, " ", name), " annotated with ", annotations);
        return join(description, " that ", and(new String[] {
            join("accepts ", arguments),
            join("returns ", type),
            join("throws ", thrown)
        }, STRING));
    }
    
    private final Match<Modifier> modifiers;
    private final Match<Class<? extends Annotation>> annotations;
    private final Match<TypeMirror> type;
    private final Sequence<VariableElement> arguments;
    private final Sequence<TypeMirror> thrown;
    
    public Method(Match<Modifier> modifiers, Match<Class<? extends Annotation>> annotations, Match<TypeMirror> type, Sequence<VariableElement> arguments, Sequence<TypeMirror> thrown) {
        this(modifiers, annotations, type, arguments, thrown, 
            format(modifiers.expectation(), "method", annotations.expectation(), type.expectation(), arguments.expectation(), thrown.expectation()), 
            format(modifiers.expectation(), "methods", annotations.expectation(), type.expectation(), arguments.expectation(), thrown.expectation())
        );
    }

    public Method(Match<Modifier> modifiers, Match<Class<? extends Annotation>> annotations, Match<TypeMirror> type, Sequence<VariableElement> arguments, Sequence<TypeMirror> thrown, String expectation, String expectations) {
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
        if (element instanceof ExecutableElement) {
            var method = (ExecutableElement) element;
            return format(
                modifiers.describe(method), "method", annotations.describe(element), 
                type.describe(method.getReturnType()), arguments.describe(method.getParameters()), thrown.describe(method.getThrownTypes())
            );
            
        } else {
            return element.getKind().toString().toLowerCase().replace('_', ' ');
        }
    }
    
    public void reset() {
        arguments.reset();
        thrown.reset();
    }
    
    
    public static class Builder {
        
        static final Sequence<VariableElement> ANY_ARGUMENTS = new AnyArguments();
        static final Sequence<TypeMirror> ANY_TYPES = new AnyTypes();
        
        private Match<Modifier> modifiers = ANY_MODIFIER;
        private Match<Class<? extends Annotation>> annotations = ANY_ANNOTATION;
        private Match<TypeMirror> type = ANY_TYPE;
        private Sequence<VariableElement> arguments = ANY_ARGUMENTS;
        private Sequence<TypeMirror> thrown = ANY_TYPES;
        private @Nullable String single;
        private @Nullable String plural;
        
        public Builder modifiers(Modifier... modifiers) {
            return modifiers(exactly(modifiers));
        }
        
        public Builder modifiers(Match<Modifier> modifiers) {
            this.modifiers = modifiers;
            return this;
        }
               
        public Builder annotations(Match<Class<? extends Annotation>> annotations) {
            this.annotations = annotations;
            return this;
        }
        
        public Builder arguments(Sequence<VariableElement> arguments) {
            this.arguments = arguments;
            return this;
        }
        
        public Builder type(Class<?> type) {
            return type(exactly(type));
        }
        
        public Builder type(Match<TypeMirror> type) {
            this.type = type;
            return this;
        }
        
        public Builder thrown(Sequence<TypeMirror> types) {
            this.thrown = types;
            return this;
        }
        
        public Builder expectation(String single, String plural) {
            this.single = single;
            this.plural = plural;
            return this;
        }
        
        public Method build() {
            if (single == null || plural == null) {
                return new Method(modifiers, annotations, type, arguments, thrown);
                
            } else {
                return new Method(modifiers, annotations, type, arguments, thrown, single, plural);
            }
        }
        
    }

}

class AnyArguments extends Sequence<VariableElement> {
    
    static final Variable variable = variable().build();
    
    AnyArguments() {
        super("");
    }

    @Override
    public boolean match(Collection<? extends Element> elements, TypeMirrors types) {
        return true;
    }

    @Override
    public String describe(Collection<? extends VariableElement> values) {
        return and(values, (value, builder) -> builder.append(variable.describe(value)));
    }

    @Override
    public void reset() {}
    
}

class AnyTypes extends Sequence<TypeMirror> {
    
    AnyTypes() {
        super("");
    }

    @Override
    public boolean match(Collection<? extends Element> elements, TypeMirrors types) {
        return true;
    }

    @Override
    public String describe(Collection<? extends TypeMirror> values) {
        return and(values, (value, builder) -> value.accept(TypePrinter.SIMPLE, builder));
    }

    @Override
    public void reset() {}
    
}

