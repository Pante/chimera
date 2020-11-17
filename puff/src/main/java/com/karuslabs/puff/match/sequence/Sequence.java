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
package com.karuslabs.puff.match.sequence;

import com.karuslabs.puff.Texts;
import com.karuslabs.puff.match.Match;
import com.karuslabs.puff.match.times.Times;
import com.karuslabs.puff.type.TypeMirrors;

import java.util.Collection;
import javax.lang.model.element.Element;

public abstract class Sequence<T> {

    public static <T> Sequence<T> contains(Times<T>... times) {
        return new ContainsSequence<>(times);
    }
    
    public static <T> Sequence<T> exactly(Match<T>... matches) {
        return new ExactSequence<>(matches);
    }
    
    public static <T> Sequence<T> each(Match<T> match) {
        return new EachSequence<>(match);
    }
    
    private final String expectation;
    
    public Sequence(String expectation) {
        this.expectation = expectation;
    }
    
    public abstract boolean match(Collection<? extends Element> elements, TypeMirrors types);
    
    public abstract String describe(Collection<? extends T> values);
    
    public abstract void reset();
    
    public String expectation() {
        return expectation;
    }
    
}

class ContainsSequence<T> extends Sequence<T> {
    
    private final Times<T>[] times;
    
    ContainsSequence(Times<T>... times) {
        super(Texts.and(times, (time, builder) -> builder.append(time.expectations())));
        this.times = times;
    }

    @Override
    public boolean match(Collection<? extends Element> elements, TypeMirrors types) {
        for (var element : elements) {
            for (var time : times) {
                time.add(types, element);
            }
        }
        
        for (var time : times) {
            if (!time.verify()) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public String describe(Collection<? extends T> values) {
        return Texts.and(times, (time, builder) -> builder.append(time.describe()));
    }
    
    @Override
    public void reset() {
        for (var time : times) {
            time.reset();
        }
    }
    
}

class ExactSequence<T> extends Sequence<T> {

    private final Match<T>[] matches;
    
    ExactSequence(Match<T>... matches) {
        super("exactly " + Texts.and(matches, (match, builder) -> builder.append(match.expectation())));
        this.matches = matches;
    }

    @Override
    public boolean match(Collection<? extends Element> elements, TypeMirrors types) {
        if (elements.size() != matches.length) {
            return false;
        }
        
        int i = 0;
        for (var element : elements) {
            if (!matches[i++].match(types, element)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public String describe(Collection<? extends T> values) {
        if (matches.length > 0 && values.isEmpty()) {
            return "empty";
            
        } else if (matches.length < values.size()) {
            return values.size() + " identifiers";
            
        } else {
            var descriptions = new String[values.size()];
            int i = 0;
            for (var value : values) {
                descriptions[i] = matches[i].describe(value);
                i++;
            }
            
            return Texts.and(descriptions, Texts.STRING);
        }
    }

    @Override
    public void reset() {}
    
}

class EachSequence<T> extends Sequence<T> {
    
    private final Match<T> match;
    
    EachSequence(Match<T> match) {
        super(match.expectations());
        this.match = match;
    }

    @Override
    public boolean match(Collection<? extends Element> elements, TypeMirrors types) {
        for (var element : elements) {
            if (!match.match(types, element)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public String describe(Collection<? extends T> values) {
        var descriptions = new String[values.size()];
        int i = 0;
        for (var value : values) {
            descriptions[i++] = match.describe(value);
        }
        
        return Texts.and(descriptions, Texts.STRING);
    }

    @Override
    public void reset() {}
    
}
