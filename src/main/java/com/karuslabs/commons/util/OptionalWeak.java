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

import java.lang.ref.*;
import java.util.*;
import java.util.function.*;


public class OptionalWeak<T> extends WeakReference<T> {
    
    public OptionalWeak(T referent) {
        this(referent, null);
    }
    
    public OptionalWeak(T referent, ReferenceQueue<? super T> queue) {
        super(referent, queue);
    }
    
    
    @Override
    public T get() {
        T referent = super.get();
        if (referent != null) {
            return referent;
            
        } else {
            throw new NoSuchElementException();
        }
    }
    
    public T getUnchecked() {
        return super.get();
    }
    
    
    public void ifPresent(Consumer<? super T> consumer) {
        T referent = super.get();
        if (referent != null) {
            consumer.accept(referent);
        }
    }
    
    public boolean isPresent() {
        return super.get() != null;
    }
    
    
    public T orElse(T other) {
        T referent = super.get();
        if (referent != null) {
            return referent;
            
        } else {
            return other;
        }
    }
    
    public T orElseGet(Supplier<? extends T> supplier) {
        T referent = super.get();
        if (referent != null) {
            return referent;
            
        } else {
            return supplier.get();
        }
    }
    
    public <GenericThrowable extends Throwable> T orElseThrow(Supplier<? extends GenericThrowable> supplier) throws GenericThrowable {
        T referent = super.get();
        if (referent != null) {
            return referent;
            
        } else {
            throw supplier.get();
        }
    }
    
    
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
            
        } else if (object instanceof OptionalWeak) {
            OptionalWeak<?> other = (OptionalWeak) object;
            return Objects.equals(super.get(), other.getUnchecked());
            
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.get());
    }
    
}
