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
package com.karuslabs.puff.match.times;

import com.karuslabs.puff.match.*;
import com.karuslabs.puff.type.TypeMirrors;

import javax.lang.model.element.Element;

public abstract class Times<T> implements Description {

    public static <T> Times<T> exactly(int times, Timeable<T> match) {
        return new Exactly<>(times, match);
    }
    
    public static <T> Times<T> between(int min, int max, Timeable<T> match) {
        return new Between<>(min, max, match);
    }
    
    public static <T> Times<T> least(int least, Timeable<T> match) {
        return new Least<>(least, match);
    }
    
    public static <T> Times<T> most(int most, Timeable<T> match) {
        return new Most<>(most, match);
    }
    
    public static <T> Times<T> no(Timeable<T> match) {
        return new No<>(match);
    }
    
    static String format(int times, Timeable<?> match) {
        if (times == 0) {
            return "no " + match.expectations();
            
        } else {
            return times + " " + match.expectations();
        }
    }
    
    protected final Timeable<T> match;
    protected final String expectation;
    protected int current;
    
    public Times(Timeable<T> match, String expectation) {
        this.match = match;
        this.expectation = expectation;
        this.current = 0;
    }
    
    public boolean add(TypeMirrors types, Element element) {
        var valid = match.match(types, element);
        if (valid) {
            current++;
        }
        
        return valid;
    }
    
    public abstract boolean verify();
    
    public void reset() {
        current = 0;
    }
    
    public String describe() {
        if (current == 0) {
            return "no " + match.expectations();
            
        } else {
            return current + " " + match.expectations();
        }
    }

    @Override
    public String expectation() {
        return expectation;
    }

}

class Exactly<T> extends Times<T> {

    private final int times;
    
    public Exactly(int times, Timeable<T> match) {
        super(match, format(times, match));
        this.times = times;
    }

    @Override
    public boolean verify() {
        return current == times;
    }

}

class Between<T> extends Times<T> {

    private final int min;
    private final int max;
    
    Between(int min, int max, Timeable<T> match) {
        super(match, "between " + min + " to " + max + " " + match.expectations());
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean verify() {
        return current > min && current <= max;
    }
    
}

class Least<T> extends Times<T> {
    
    private final int least;
    
    Least(int least, Timeable<T> match) {
        super(match, "at least " + least + " " + match.expectations());
        this.least = least;
    }

    @Override
    public boolean verify() {
        return current >= least;
    }
    
}

class Most<T> extends Times<T> {
    
    private final int most;
    
    Most(int most, Timeable<T> match) {
        super(match, "at most " + most + " " + match.expectations());
        this.most = most;
    }

    @Override
    public boolean verify() {
        return current <= most;
    }
    
}

class No<T> extends Times<T> {
    
    No(Timeable<T> match) {
        super(match, "no " + match.expectations());
    }

    @Override
    public boolean verify() {
        return current == 0;
    }
    
}
