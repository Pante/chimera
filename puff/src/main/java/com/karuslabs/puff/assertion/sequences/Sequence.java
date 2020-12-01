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
package com.karuslabs.puff.assertion.sequences;

import com.karuslabs.puff.Texts;
import com.karuslabs.puff.assertion.*;
import com.karuslabs.puff.assertion.matches.Match;
import com.karuslabs.puff.assertion.times.Times;
import com.karuslabs.puff.type.TypeMirrors;

import java.util.Collection;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public abstract class Sequence<T> extends SkeletonAssertion {
    
    public static final Sequence<VariableElement> ANY_PARAMETERS = new AnyParameter();
    public static final Sequence<TypeMirror> ANY_TYPES = new AnyType();
    
    public static <T> Sequence<T> contains(Times<T>... times) {
        return new ContainsSequence<>(times);
    }
    
    public static <T> Sequence<T> match(Times<T>... times) {
        return new MatchTimeSequence<>(times);
    }
    
    public static <T> Sequence<T> match(Match<T>... matches) {
        return new MatchSequence<>(matches);
    }
    
    public static <T> Sequence<T> each(Match<T> match) {
        return new EachSequence<>(match);
    }
    
    
    protected static String format(Assertion... assertions) {
        return Texts.join(assertions, (assertion, builder) -> builder.append('[').append(assertion.condition()).append(']'), ", ");
    }

    
    public Sequence(String condition) {
        super(condition);
    }
    
    public abstract boolean test(TypeMirrors types, Collection<? extends T> values);
    
    public abstract String describe(TypeMirrors types, Collection<? extends T> values);
    
}

class MatchSequence<T> extends Sequence<T> {

    private final Match<T>[] matches;
    
    MatchSequence(Match<T>... matches) {
        super("match " +  format(matches));
        this.matches = matches;
    }

    @Override
    public boolean test(TypeMirrors types, Collection<? extends T> values) {
        if (values.size() != matches.length) {
            return false;
        }
        
        int i = 0;
        for (var value : values) {
            if (!matches[i++].test(types, value)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public String describe(TypeMirrors types, Collection<? extends T> values) {
        if (values.isEmpty()) {
            return "";
            
        } else if (matches.length == 0) {
            return values.size() == 1 ? "1 value" : values.size() + " values";
            
        } else {
            var descriptions = new String[values.size()];
            int i = 0;
            for (var value : values) {
                descriptions[i++] = matches[0].describe(value);
            }
            
            return Texts.join(descriptions, Texts.STRING, ", ");
        }
    }
    
}

class EachSequence<T> extends Sequence<T> {
    
    private final Match<T> match;
    
    EachSequence(Match<T> match) {
        super("each [" + match.condition() + "]");
        this.match = match;
    }

    @Override
    public boolean test(TypeMirrors types, Collection<? extends T> values) {
        for (var value : values) {
            if (!match.test(types, value)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public String describe(TypeMirrors types, Collection<? extends T> values) {
        var descriptions = new String[values.size()];
        int i = 0;
        for (var value : values) {
            descriptions[i++] = match.describe(value);
        }
        
        return Texts.join(descriptions, Texts.STRING, ", ");
    }
    
}
