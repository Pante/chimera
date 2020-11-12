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
package com.karuslabs.puff.old;

import com.karuslabs.puff.Format;
import com.karuslabs.puff.type.TypeMirrors;
import com.karuslabs.puff.old.Description;

import java.util.*;
import javax.lang.model.element.Element;

import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class Contents<T> implements Description {
    
    public static <T> Contents<T> types(SubjectContents<T> contents) {
        return contents.set("type", "types");
    }
    
    public static <T> Contents<T> methods(SubjectContents<T> contents) {
        return contents.set("method", "methods");
    }
    
    public static <T> Contents<T> arguments(SubjectContents<T> contents) {
        return contents.set("argument", "arguments");
    }
    
    public static <T> Contents<T> fields(SubjectContents<T> contents) {
        return contents.set("field", "fields");
    }
    
    public static <T> Contents<T> variables(SubjectContents<T> contents) {
        return contents.set("variable", "variables");
    }
    
    
    public static <T> Contents<T> contains(Times<T>... times) {
        return new ContainsContents(times);
    }
    
    public static <T> SubjectContents<T> exactly(Match<T>... matches) {
        return new ExactlyContents<>(matches);
    }
    
    public static <T> Contents<T> each(Match<T> match) {
        return new EachContents<>(match);
    }
    
    private final String condition;
    
    public Contents(String condition) {
        this.condition = condition;
    }
    
    public abstract @Nullable String match(Collection<? extends Element> elements, TypeMirrors types);
    
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
        return condition;
    }
    
}

class ContainsContents<T> extends Contents<T> {
    
    private final Times<T>[] times;
    
    ContainsContents(Times<T>... times) {
        super(Format.and(List.of(times), (time, builder) -> builder.append(time.condition())));
        this.times = times;
    }
    
    @Override
    public @Nullable String match(Collection<? extends Element> elements, TypeMirrors types) {
        for (var element : elements) {
            for (var time : times) {
                time.add(element, types);
            }
        }
        
        var errors = new ArrayList<String>();
        for (var time : times) {
            var error = time.match();
            if (error != null) {
                errors.add(error);
            }
            
            time.reset();
        }
        
        if (!errors.isEmpty()) {
            return Format.and(errors, (error, builder) -> builder.append(error));
            
        } else {
            return null;
        }
    }
    
}

class ExactlyContents<T> extends SubjectContents<T> {

    private final Match<T>[] matches;
    
    ExactlyContents(Match<T>... matches) {
        super("exactly " + Format.and(List.of(matches), (match, builder) -> builder.append(match.condition())));
        this.matches = matches;
    }
    
    
    @Override
    public @Nullable String match(Collection<? extends Element> elements, TypeMirrors types) {
        if (matches.length > 0 && elements.isEmpty()) {
            return "empty";
            
        } else if (matches.length < elements.size()) {
            return elements.size() + " " + plural;
            
        } else if (matches.length > elements.size()) {
            return format(elements, types);
        }
        
        var i = 0;
        for (var element : elements) {
            if (matches[i++].match(element, types) != null) {
                return format(elements, types);
            }
        }
        
        return null;
    }
    
    String format(Collection<? extends Element> elements, TypeMirrors types) {
        var list = new ArrayList<String>();
        var i = 0;
        for (var element : elements) {
            var match = matches[i++];
            var error = match.match(element, types);
            list.add(error == null ? match.condition() : error);
        }
        
        return Format.and(list, (e, builder) -> builder.append(e));
    }
    
}

class EachContents<T> extends Contents<T> {
    
    private final Match<T> match;
    
    EachContents(Match<T> match) {
        super(match.condition());
        this.match = match;
    }

    @Override
    public @Nullable String match(Collection<? extends Element> elements, TypeMirrors types) {
        for (var element : elements) {
            var error = match.match(element, types);
            if (error != null) {
                return error;
            }
        }
        
        return null;
    }
    
}