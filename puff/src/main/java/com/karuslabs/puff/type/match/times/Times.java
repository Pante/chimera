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
package com.karuslabs.puff.type.match.times;

import com.karuslabs.puff.type.TypeMirrors;
import com.karuslabs.puff.type.match.Description;
import com.karuslabs.puff.type.match.matches.Match;

import javax.lang.model.element.Element;

import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class Times<T> implements Description {
    
    public static <T> Times<T> exactly(int times, Match<T> match) {
        return new Exactly<>(times, match);
    }
    
    public static <T> Times<T> between(int min, int max, Match<T> match) {
        return new Between<>(min, max, match);
    }
    
    public static <T> Times<T> least(int least, Match<T> match) {
        return new Least<>(least, match);
    }
    
    public static <T> Times<T> most(int most, Match<T> match) {
        return new Most<>(most, match);
    }
    
    public static <T> Times<T> no(Match<T> match) {
        return new No<>(match);
    }
    
    static String format(int times, Match<?> match) {
        if (times == 0) {
            return "no " + match.plural();
            
        } else {
            return times + " " + match.plural();
        }
    }
    
    protected final Match<T> match;
    protected final String condition;
    protected int current;
    
    public Times(Match<T> match, String condition) {
        this.match = match;
        this.condition = condition;
        this.current = 0;
    }
    
    public abstract @Nullable String match();
    
    public void add(Element element, TypeMirrors types) {
        if (match.match(element, types) == null) {
            current++;
        }
    }
    
    public void reset() {
        current = 0;
    }
    
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

class Exactly<T> extends Times<T> {

    private final int times;
    
    public Exactly(int times, Match<T> match) {
        super(match, format(times, match));
        this.times = times;
    }

    @Override
    public @Nullable String match() {
        return current == times ? null : format(current, match);
    }

}

class Between<T> extends Times<T> {

    private final int min;
    private final int max;
    
    Between(int min, int max, Match<T> match) {
        super(match, "between " + min + " to " + max + " " + match.plural());
        this.min = min;
        this.max = max;
    }

    @Override
    public String match() {
        return current > min && current <= max ? null : format(current, match);
    }
    
}

class Least<T> extends Times<T> {
    
    private final int least;
    
    Least(int least, Match<T> match) {
        super(match, "at least " + least + " " + match.plural());
        this.least = least;
    }

    @Override
    public @Nullable String match() {
        return current >= least ? null : format(current, match);
    }
    
}

class Most<T> extends Times<T> {
    
    private final int most;
    
    Most(int most, Match<T> match) {
        super(match, "at most " + most + " " + match.plural());
        this.most = most;
    }

    @Override
    public @Nullable String match() {
        return current <= most ? null : format(current, match);
    }
    
}

class No<T> extends Times<T> {
    
    No(Match<T> match) {
        super(match, "no " + match.plural());
    }

    @Override
    public String match() {
        return current == 0 ? null : format(current, match);
    }
    
}
