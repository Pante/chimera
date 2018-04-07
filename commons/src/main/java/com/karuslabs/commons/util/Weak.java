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

import com.karuslabs.annotations.ValueBased;
import com.karuslabs.annotations.Immutable;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;
import javax.annotation.Nullable;

import static java.util.stream.Stream.*;


@Immutable
@ValueBased
public final class Weak<T> extends WeakReference<T> {
    
    public static final Weak<?> EMPTY = new Weak<>(null);

    
    
    public Weak(T value) {
        super(value);
    }
    
    
    @Override
    public T get() {
        T value = super.get();
        if (value != null) {
            return value;
            
        } else {
            throw new NoSuchElementException();
        }
    }
    
    public @Nullable T getNullable() {
        return super.get();
    }
    
    
    public void ifPreset(Consumer<? super T> consumer) {
        T value = super.get();
        if (value != null) {
            consumer.accept(value);
        }
    }
    
    public void ifPresentOrElse(Consumer<? super T> consumer, Runnable empty) {
        T value = super.get();
        if (value != null) {
            consumer.accept(value);
            
        } else {
            empty.run();
        }
    }
    
    public boolean isPresent() {
        return super.get() != null;
    }
    
    
    public Weak<T> or(Supplier<Weak<T>> supplier) {
        if (super.get() != null) {
            return this;
        } else {
            return supplier.get();
        }
    }
    
    public T orElse(T other) {
        return Get.orDefault(super.get(), other);
    }
    
    public T orElseGet(Supplier<T> supplier) {
        return Get.orDefault(super.get(), supplier);
    }
    
    public <E extends RuntimeException> T orElseThrow(Supplier<E> supplier) {
        return Get.orThrow(super.get(), supplier);
    }
    
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
