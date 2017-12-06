/*
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
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

import com.karuslabs.commons.annotation.*;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;
import javax.annotation.Nullable;

import static java.util.stream.Stream.*;


/**
 * A container object which may or may not hold a weak reference to a value.
 * If a value is non-null, {@code isPresent} will return {@code true} and {@link #get()} will return the value.
 * 
 * Additional methods that depend on the presence or absence of a contained value are provided, 
 * such as {@code orElse()} (return a default value if value not present) and {@code ifPresent()} (execute a block of code if the value is present). 
 * 
 * @param <T> the type of the value
 */
@Immutable
@ValueBased
public final class Weak<T> extends WeakReference<T> {
    
    /**
     * An empty instance of {@code Weak}
     */
    public static final Weak<?> EMPTY = new Weak<>(null);

    
    
    /**
     * Constructs a {@code Weak} which holds a weak reference to the specified {@code value}.
     * 
     * @param value the value to which this Weak holds a weak reference
     */
    public Weak(T value) {
        super(value);
    }
    
    
    /**
     * Returns the value held by this {@code Weak}, or throws {@code NoSuchElementException} if the value is null.
     * 
     * @return the value held by this Weak if not null
     * @throws NoSuchElementException if the value in this Weak is null
     */
    @Override
    public T get() {
        T value = super.get();
        if (value != null) {
            return value;
            
        } else {
            throw new NoSuchElementException();
        }
    }
    
    /**
     * Returns the value held by this {@code Weak}
     * 
     * @return the value held by this Weak
     */
    public @Nullable T getNullable() {
        return super.get();
    }
    
    
    /**
     * Invokes the specified {@code consumer}, or do nothing if the value held by this {@code Weak} is null.
     * 
     * @param consumer the Consumer to invoke if the value held by this Weak is non-null
     */
    public void ifPreset(Consumer<? super T> consumer) {
        T value = super.get();
        if (value != null) {
            consumer.accept(value);
        }
    }
    
    /**
     * Invokes the specified {@code consumer}, or executes the specified {@code Runnable} 
     * if the value held by this {@code Weak} is null.
     * 
     * @param consumer the Consumer to invoke if the value held by this Weak is non-null
     * @param empty the Runnable to execute if the value held by this Weak is null
     */
    public void ifPresentOrElse(Consumer<? super T> consumer, Runnable empty) {
        T value = super.get();
        if (value != null) {
            consumer.accept(value);
            
        } else {
            empty.run();
        }
    }
    
    /**
     * Returns {@code true} if the value held by this {@code Weak} is non-null, else {@code false}.
     * 
     * @return true, if the value is non-null; else false
     */
    public boolean isPresent() {
        return super.get() != null;
    }
    
    
    /**
     * Returns {@code this}, or a {@code Weak} produced by the specified {@code Supplier} if the value held by this {@code Weak} is null.
     * 
     * @param supplier the supplying function that produces a Weak to be returned
     * @return this, if the value held is non-null; else a Weak produced by the specified Supplier
     */
    public Weak<T> or(Supplier<Weak<T>> supplier) {
        if (super.get() != null) {
            return this;
        } else {
            return supplier.get();
        }
    }
    
    /**
     * Returns the value held by this {@code Weak}, or {@code other} if the value is null.
     * 
     * @param other the value to be returned if the value held by this Weak is null
     * @return the value, if non-null; else other
     */
    public T orElse(T other) {
        return Get.orDefault(super.get(), other);
    }
    
    /**
     * Returns the value held by this {@code Weak}, or the value produced by the specified {@code Supplier} if the value is null.
     * 
     * @param supplier the supplying function which produces a value to be returned
     * @return the value, if non-null; else the value produced by the specified Supplier
     */
    public T orElseGet(Supplier<T> supplier) {
        return Get.orDefault(super.get(), supplier);
    }
    
    /**
     * Returns the value held by this {@code Weak}, or throws the exception produced by the specified {@code Supplier} if the value is null.
     * 
     * @param <E> the type of the exception to throw
     * @param supplier the supplying function which produces an exception to throw
     * @return the value, if non-null
     * @throws E if the value held by this Weak is null
     */
    public <E extends RuntimeException> T orElseThrow(Supplier<E> supplier) {
        return Get.orThrow(super.get(), supplier);
    }
    
    /**
     * Returns a singleton stream, or an empty stream if the value held by this {@code Weak} is null.
     * 
     * @return a singleton stream containing the value held by this Weak if non-null; else an empty stream
     */
    public Stream<T> stream() {
        T value = super.get();
        if (value != null) {
            return of(value);
            
        } else {
            return empty();
        }
    }
    
    
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
            
        } else if (object instanceof Weak) {
            Weak<?> other = (Weak<?>) object;
            return Objects.equals(super.get(), other.getNullable());
            
        } else {
            return false;
        }        
    }
    
    @Override
    public int hashCode() {
        T value = super.get();
        if (value != null) {
            return value.hashCode();
            
        } else {
            return 0;
        }
    }
    
    @Override
    public String toString() {
        T value = super.get();
        return value != null ? String.format("Weak[%s]", value) : "Weak.empty";
    }
    
}
