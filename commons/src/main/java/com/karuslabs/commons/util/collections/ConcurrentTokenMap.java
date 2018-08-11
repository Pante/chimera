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
package com.karuslabs.commons.util.collections;

import com.karuslabs.commons.util.collections.TokenMap.Key;

import java.util.concurrent.*;


public interface ConcurrentTokenMap<T, N> extends TokenMap<T, N> {
    
    public static <T, N> ConcurrentTokenMap<T, N> of() {
        return new TokenConcurrentHashMap<>();
    }
    
    public static <T, N> ConcurrentTokenMap<T, N> of(int capacity) {
        return new TokenConcurrentHashMap<>(capacity);
    }
    
    public static <T, N> ConcurrentTokenMap<T, N> of(ConcurrentMap<Key<? extends T, N>, T> map) {
        return new TokenProxiedConcurrentMap<>(map);
    }
    
    
    @Override
    public ConcurrentMap<Key<? extends T, N>, T> map();
    
}


class TokenConcurrentHashMap<T, N> extends ConcurrentHashMap<Key<? extends T, N>, T> implements ConcurrentTokenMap<T, N> {
    
    TokenConcurrentHashMap() {}
    
    TokenConcurrentHashMap(int capacity) {
        super(capacity);
    }
    
    
    @Override
    public ConcurrentMap<Key<? extends T, N>, T> map() {
        return this;
    }
    
}


class TokenProxiedConcurrentMap<T, N> implements ConcurrentTokenMap<T, N> {
    
    ConcurrentMap<Key<? extends T, N>, T> map;
    
    
    TokenProxiedConcurrentMap(ConcurrentMap<Key<? extends T, N>, T> map) {
        this.map = map;
    }
    
    
    @Override
    public ConcurrentMap<Key<? extends T, N>, T> map() {
        return map;
    }
    
}
