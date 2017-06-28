/*
 * Copyright (C) 2017 Karus Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
