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

/**
 * A value-based container that holds a weak reference to a value and contains methods 
 * similar to {@code Optional}.
 * 
 * @author Javadoc copied from {@link java.util.Optional}
 * 
 * @param <T> the type of the value
 */
public interface Weak<T> {
    
    /**
     * Creates a {@code Weak} with the given value.
     * 
     * @param <T> the type of the value
     * @param value the value
     * @return a {@code Weak}
     */
    static <T> Weak<T> of(T value) {
        return new WeakValue<>(value);
    }
    
    /**
     * Returns an empty {@code Weak}.
     * 
     * @param <T> the type of the value
     * @return the {@code Weak}
     */
    static <T> Weak<T> empty() {
        return (Weak<T>) EMPTY;
    }
    
    /**
     * If a value is present, and the value matches the given predicate,
     * returns an {@code Weak} describing the value, otherwise returns an
     * empty {@code Weak}.
     *
     * @param predicate the predicate to apply to a value, if present
     * @return an {@code Weak} describing the value of this
     *         {@code Weak}, if a value is present and the value matches the
     *         given predicate, otherwise an empty {@code Weak}
     * @throws NullPointerException if the predicate is {@code null}
     */
    Weak<T> filter(Predicate<? super T> predicate);
    
    /**
     * If a value is present, returns the result of applying the given
     * {@code Weak}-bearing mapping function to the value, otherwise returns
     * an empty {@code Weak}.
     *
     * <p>This method is similar to {@link #map(Function)}, but the mapping
     * function is one whose result is already an {@code Weak}, and if
     * invoked, {@code flatMap} does not wrap it within an additional
     * {@code Weak}.
     *
     * @param <U> The type of value of the {@code Weak} returned by the
     *            mapping function
     * @param mapper the mapping function to apply to a value, if present
     * @return the result of applying an {@code Weak}-bearing mapping
     *         function to the value of this {@code Weak}, if a value is
     *         present, otherwise an empty {@code Weak}
     * @throws NullPointerException if the mapping function is {@code null} or
     *         returns a {@code null} result
     */
    <U> Weak<U> flatMap(Function<? super T, ? extends Weak<? extends U>> mapper);
    
    /**
     * If a value is present, returns an {@code Weak} describing the result of 
     * applying the given mapping function to the value, otherwise returns an empty {@code Weak}.
     *
     * <p>If the mapping function returns a {@code null} result then this method
     * returns an empty {@code Weak}.
     *
     * <pre>{@code
     *     Weak<Path> p =
     *         uris.stream().filter(uri -> !isProcessedYet(uri))
     *                       .findFirst()
     *                       .map(Paths::get);
     * }</pre>
     *
     * Here, {@code findFirst} returns an {@code Weak<URI>}, and then
     * {@code map} returns an {@code Weak<Path>} for the desired
     * URI if one exists.
     *
     * @param mapper the mapping function to apply to a value, if present
     * @param <U> The type of the value returned from the mapping function
     * @return an {@code Weak} describing the result of applying a mapping
     *         function to the value of this {@code Weak}, if a value is
     *         present, otherwise an empty {@code Weak}
     * @throws NullPointerException if the mapping function is {@code null}
     */
    <U> Weak<U> map(Function<? super T, ? extends U> mapper);
    
    /**
     * If a value is present, returns the value, otherwise returns
     * {@code other}.
     *
     * @param other the value to be returned, if no value is present.
     *        May be {@code null}.
     * @return the value, if present, otherwise {@code other}
     */
    Weak<T> orElse(Supplier<? extends Weak<? extends T>> other);
    
    /**
     * If a value is present, returns the value, otherwise returns
     * {@code other}.
     *
     * @param other the value to be returned, if no value is present.
     *        May be {@code null}.
     * @return the value, if present, otherwise {@code other}
     */
    T or(T other);
    
    /**
     * If a value is present, returns the value, otherwise returns the result
     * produced by the supplying function.
     *
     * @param other the supplying function that produces a value to be returned
     * @return the value, if present, otherwise the result produced by the
     *         supplying function
     * @throws NullPointerException if no value is present and the supplying
     *         function is {@code null}
     */
    T or(Supplier<T> other);
    
    /**
     * If a value is present, returns the value, otherwise throws
     * {@code NoSuchElementException}.
     *
     * @return the non-{@code null} value described by this {@code Weak}
     * @throws NoSuchElementException if no value is present
     */
    T orThrow();
    
    /**
     * If a value is present, returns the value, otherwise throws an exception
     * produced by the exception supplying function.
     *
     * @param <E> type of the exception to be thrown
     * @param exception the supplying function that produces an
     *        exception to be thrown
     * @return the value, if present
     * @throws E the exception to be thrown
     * @throws NullPointerException if no value is present and the exception
     *          supplying function is {@code null}
     */
    <E extends Throwable> T orThrow(Supplier<? extends E> exception) throws E;
            
    /**
     * If a value is present, performs the given action with the value,
     * otherwise does nothing.
     *
     * @param action the action to be performed, if a value is present
     * @throws NullPointerException if value is present and the given action is
     *         {@code null}
     */
    void ifPresent(Consumer<? super T> action);
    
    /**
     * If a value is present, performs the given action with the value,
     * otherwise performs the given empty-based action.
     *
     * @param action the action to be performed, if a value is present
     * @param otherwise the empty-based action to be performed, if no value is
     *        present
     * @throws NullPointerException if a value is present and the given action
     *         is {@code null}, or no value is present and the given empty-based
     *         action is {@code null}.
     */
    void ifPresent(Consumer<? super T> action, Runnable otherwise);
    
    /**
     * If a value is present, returns {@code true}, otherwise {@code false}.
     *
     * @return {@code true} if a value is present, otherwise {@code false}
     */
    boolean isPresent();
    
    /**
     * If a value is present, returns a sequential {@link Stream} containing
     * only that value, otherwise returns an empty {@code Stream}.
     *
     * @return the optional value as a {@code Stream}
     */
    Stream<T> stream();
    
}

/**
 * A {@code Weak} that is also a {@code WeakReference}.
 * 
 * @param <T> the type of the value
 */
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
        return this == other || other instanceof Weak weak && Objects.equals(get(), weak.or(null));
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

