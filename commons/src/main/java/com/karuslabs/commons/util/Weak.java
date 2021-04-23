/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
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
package com.karuslabs.commons.util;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

import static com.karuslabs.commons.util.WeakValue.EMPTY;
import static java.util.stream.Stream.ofNullable;

public interface Weak<T> {
    
    static <T> Weak<T> of(T value) {
        return new WeakValue<>(value);
    }
    
    static <T> Weak<T> empty() {
        return (Weak<T>) EMPTY;
    }
    
    
    Weak<T> filter(Predicate<? super T> predicate);
    
    <U> Weak<U> flatMap(Function<? super T, ? extends Weak<? extends U>> mapper);
    
    <U> Weak<U> map(Function<? super T, ? extends U> mapper);
    
    
    Weak<T> orElse(Supplier<? extends Weak<? extends T>> other);
    
    
    T or(T other);
    
    T or(Supplier<T> other);
    
    T orThrow();
    
    <E extends Throwable> T orThrow(Supplier<? extends E> exception) throws E;
            
    
    void ifPresent(Consumer<? super T> action);
    
    void ifPresent(Consumer<? super T> action, Runnable otherwise);
    
    boolean isPresent();
    
    
    Stream<T> stream();
    
}

final class WeakValue<T> extends WeakReference<T> implements Weak<T> {
    
    static final Weak<?> EMPTY = new WeakValue<>(null);
    
    WeakValue(T referent) {
        super(referent);
    }

    
    @Override
    public Weak<T> filter(Predicate<? super T> predicate) {
       T value = get();
       return value != null && predicate.test(value) ? this : (Weak<T>) EMPTY;
    }

    @Override
    public <U> Weak<U> flatMap(Function<? super T, ? extends Weak<? extends U>> mapper) {
        T value = get();
        return value != null ? (Weak<U>) mapper.apply(value) : (Weak<U>) EMPTY;
    }
    
    @Override
    public <U> Weak<U> map(Function<? super T, ? extends U> mapper) {
        T value = get();
        return value != null ? new WeakValue<>(mapper.apply(value)) : (Weak<U>) EMPTY;
    }

    
    @Override
    public Weak<T> orElse(Supplier<? extends Weak<? extends T>> other) {
        T value = get();
        return value != null ? this : (Weak<T>) other.get();
    }

    @Override
    public T or(T other) {
        T value = get();
        return value != null ? value : other;
    }

    @Override
    public T or(Supplier<T> other) {
        T value = get();
        return value != null ? value : other.get();
    }

    @Override
    public T orThrow() {
        T value = get();
        if (value != null) {
            return value;
        } else {
            throw new NoSuchElementException("Value was reclaimed");
        }
    }

    @Override
    public <E extends Throwable> T orThrow(Supplier<? extends E> exception) throws E {
        T value = get();
        if (value != null) {
            return value;
        } else {
            throw exception.get();
        }
    }

    
    @Override
    public void ifPresent(Consumer<? super T> action) {
        T value = get();
        if (value != null) {
            action.accept(value);
        }
    }

    @Override
    public void ifPresent(Consumer<? super T> action, Runnable otherwise) {
        T value = get();
        if (value != null) {
            action.accept(value);
        } else {
            otherwise.run();
        }
    }

    @Override
    public boolean isPresent() {
        return get() != null;
    }

    
    @Override
    public Stream<T> stream() {
        return ofNullable(get());
    }
    
    
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Weak<?>)) {
            return false;
        }

        return Objects.equals(get(), ((Weak<?>) other).or(null));
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(get());
    }
    
    @Override
    public String toString() {
        T value = get();
        return value != null ? "Weak[" + value + "]" : "Weak.empty";
    }

}

