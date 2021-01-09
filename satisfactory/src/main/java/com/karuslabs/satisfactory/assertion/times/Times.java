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
package com.karuslabs.Satisfactory.assertion.times;

import com.karuslabs.Satisfactory.assertion.SkeletonAssertion;
import com.karuslabs.Satisfactory.assertion.matches.*;
import com.karuslabs.Satisfactory.type.TypeMirrors;

import javax.lang.model.element.Element;

public abstract class Times<T> extends SkeletonAssertion {
    
    public static <T> Times<T> no(Timeable<T> match) {
        return exactly(0, match);
    }
    
    public static <T> Times<T> exactly(int times, Timeable<T> match) {
        return new Exactly<>(times, match);
    }
    
    public static <T> Times<T> between(int min, int max, Timeable<T> match) {
        return new Between<>(min, max, match);
    }
    
    public static <T> Times<T> min(int min, Timeable<T> match) {
        return new Min<>(min, match);
    }
    
    public static <T> Times<T> max(int max, Timeable<T> match) {
        return new Max<>(max, match);
    }
    
    
    static String format(int times, Timeable<?> match) {
        if (times == 1) {
            return "1 " + match.condition();
            
        } else {
            return times + " " + match.conditions();
        }
    }
    
    protected final Timeable<T> match;
    protected int current;
    
    public Times(Timeable<T> match, String condition) {
        super(condition);
        this.match = match;
        this.current = 0;
    }
    
    public abstract boolean test();
    
    
    public boolean add(TypeMirrors types, Element element) {
        return increment(match.test(types, element));
    }
    
    public boolean add(TypeMirrors types, T value) {
        return increment(match.test(types, value));
    }
    
    private boolean increment(boolean valid) {
        if (valid) {
            current++;
        }
        
        return valid;
    }
    
    
    public void reset() {
        current = 0;
    }
    
    
    public String describe() {
        return format(current, match);
    }

}

class Exactly<T> extends Times<T> {

    private final int times;
    
    public Exactly(int times, Timeable<T> match) {
        super(match, format(times, match));
        this.times = times;
    }

    @Override
    public boolean test() {
        return current == times;
    }

}

class Between<T> extends Times<T> {

    private final int min;
    private final int max;
    
    Between(int min, int max, Timeable<T> match) {
        super(match, min + " to " + max + " " + match.conditions());
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean test() {
        return current >= min && current < max;
    }
    
}

class Min<T> extends Times<T> {
    
    private final int min;
    
    Min(int min, Timeable<T> match) {
        super(match, min + " or more " + match.conditions());
        this.min = min;
    }

    @Override
    public boolean test() {
        return current >= min;
    }
    
}

class Max<T> extends Times<T> {
    
    private final int max;
    
    Max(int max, Timeable<T> match) {
        super(match, max + " or more " + match.conditions());
        this.max = max;
    }

    @Override
    public boolean test() {
        return current <= max;
    }
    
}
