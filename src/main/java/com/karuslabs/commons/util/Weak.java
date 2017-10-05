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

import static java.util.stream.Stream.*;


/**
 * A container object which may or may not hold a weak reference to a non-null value.
 * If a value is present, {@code isPresent} will return {@code true} and {@code get()} will return the value.
 * 
 * Additional methods that depend on the presence or absence of a contained value are provided, 
 * such as {@code orElse()} (return a default value if value not present) and {@code ifPresent()} (execute a block of code if the value is present). 
 * 
 * @param <T> the type of the value
 */
@Immutable
@ValueBased
public final class Weak<T> {
    
    public static final Weak<?> EMPTY = new Weak<>(null);
    
    
    private final WeakReference<T> reference;
    
    
    public Weak(T value) {
        reference = new WeakReference<>(value);
    }
    
    
    public T get() {
        T value = reference.get();
        if (value != null) {
            return value;
            
        } else {
            throw new NoSuchElementException();
        }
    }    
    
    
    public void ifPreset(Consumer<? super T> consumer) {
        T value = reference.get();
        if (value != null) {
            consumer.accept(value);
        }
    }
    
    public void ifPresentOrElse(Consumer<? super T> consumer, Runnable empty) {
        T value = reference.get();
        if (value != null) {
            consumer.accept(value);
            
        } else {
            empty.run();
        }
    }
    
    public boolean isPresent() {
        return reference.get() != null;
    }
    
    
    public Weak<T> or(Supplier<Weak<T>> supplier) {
        if (reference.get() != null) {
            return this;
        } else {
            return supplier.get();
        }
    }
    
    public T orElse(T other) {
        return Get.orDefault(reference.get(), other);
    }
    
    public T orElseGet(Supplier<T> supplier) {
        return Get.orDefault(reference.get(), supplier);
    }
    
    public <E extends RuntimeException> T orElseThrow(Supplier<E> supplier) {
        return Get.orThrow(reference.get(), supplier);
    }
    
    public Stream<T> stream() {
        T value = reference.get();
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
            return Objects.equals(reference.get(), other.reference.get());
            
        } else {
            return false;
        }        
    }
    
    @Override
    public int hashCode() {
        T value = reference.get();
        if (value != null) {
            return value.hashCode();
            
        } else {
            return 0;
        }
    }
    
    @Override
    public String toString() {
        T value = reference.get();
        return value != null ? String.format("Weak[%s]", value) : "Weak.empty";
    }
    
}
