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
package com.karuslabs.puff.assertion.matches;

<<<<<<< Updated upstream:puff/src/main/java/com/karuslabs/puff/assertion/matches/Timeable.java
import java.util.function.Supplier;
=======
<<<<<<< Updated upstream:puff/src/main/java/com/karuslabs/puff/match/Timeable.java
import com.karuslabs.puff.match.matches.*;
>>>>>>> Stashed changes:puff/src/main/java/com/karuslabs/puff/match/Timeable.java

public interface Timeable<T> extends Match<T> {

    public default Timeable<T> and(Supplier<? extends Timeable<T>> other) {
        return and(other.get());
    }
    
    public default Timeable<T> and(Timeable<T> other) {
        return new And<>(this, other);
    }
    
     public default Timeable<T> or(Supplier<? extends Timeable<T>> other) {
        return and(other.get());
    }
    
    public default Timeable<T> or(Timeable<T> other) {
        return new Or<>(this, other);
    }
=======
import com.karuslabs.puff.match.Description;
import com.karuslabs.puff.type.TypeMirrors;

import java.util.Collection;
import javax.lang.model.element.Element;

public abstract class Seqeunce<T> implements Description {

    public abstract boolean match(Collection<? extends Element> elements, TypeMirrors types);
    
    public abstract void reset();
    
    public abstract String describe(Collection<? extends Element> elements);
>>>>>>> Stashed changes:puff/src/main/java/com/karuslabs/puff/match/sequence/Seqeunce.java
    
}
