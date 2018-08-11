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

import com.karuslabs.annotations.ValueBased;
import com.karuslabs.commons.util.collections.TokenMap.Key;

import java.util.*;

import static com.karuslabs.commons.util.collections.TokenMap.key;


public interface TokenMap<T, N> {
    
    public static <T, N> TokenMap<T, N> of() {
        return new TokenHashMap<>();
    }
    
    public static <T, N> TokenMap<T, N> of(int capacity) {
        return new TokenHashMap<>(capacity);
    }
    
    public static <T, N> TokenMap<T, N> of(Map<Key<? extends T, N>, T> map) {
        return new TokenProxiedMap<>(map);
    }
    
    
    public default <U extends T> boolean containsKey(Class<U> type, N name) {
        return map().containsKey(key(type, name));
    }
    
    public default <U extends T> boolean containsKey(Key<U, N> key) {
        return map().containsKey(key);
    }
    
    public default <U extends T> boolean containsValue(U value) {
        return map().containsValue(value);
    }
    
    
    public default <U extends T> U get(Class<U> type, N name) {
        return get(key(type, name));
    }
    
    public default <U extends T> U get(Key<U, N> key) {
        return (U) map().get(key);
    }
    
    
    public default <U extends T> U getOrDefault(Class<U> type, N name, U value) {
        return getOrDefault(key(type, name), value);
    }
    
    public default <U extends T> U getOrDefault(Key<U, N> key, U value) {
        T item = map().get(key);
        if (item != null && key.type.isAssignableFrom(item.getClass())) {
            return (U) item;
        } else {
            return value;
        }
    }
    
    
    public default <U extends T> U put(Class<U> type, N name, U value) {
        return put(key(type, name), value);
    }
    
    public default <U extends T> U put(Key<U, N> key, U value) {
        return (U) map().put(key, value);
    }
    
    
    public Map<Key<? extends T, N>, T> map();
    
    
    public static <T, N> Key<T, N> key(Class<T> type, N name) {
        return new Key<>(type, name);
    }
    
    public @ValueBased final class Key<T, N> {

        Class<? extends T> type;
        N name;
        int hash;

        Key(Class<T> type, N name) {
            this.type = type;
            this.name = name;
            this.hash = hash();
        }

        Key<? extends T, N> set(Class<? extends T> type, N name) {
            this.type = type;
            this.name = name;
            this.hash = hash();
            return this;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;

            } else if (object instanceof Key<?, ?> && hashCode() == object.hashCode()) {
                Key key = (Key) object;
                return type == key.type && name.equals(key.name);

            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return hash;
        }

        int hash() {
            int hash = 5;
            hash = 53 * hash + Objects.hashCode(name);
            hash = 53 * hash + Objects.hashCode(type);
            return hash;
        }

        @Override
        public String toString() {
            return "Key[name: " + name + " class: " + type.getName() + "]";
        }

    }
    
}


class TokenHashMap<T, N> extends HashMap<Key<? extends T, N>, T> implements TokenMap<T, N> {
    
    Key<T, N> cached;
    
    
    TokenHashMap() {
        cached = key(null, null);
    }
    
    TokenHashMap(int capacity) {
        super(capacity);
        cached = key(null, null);
    }
    
    
    @Override
    public <U extends T> boolean containsKey(Class<U> type, N name) {
        return containsKey(cached.set(type, name));
    }

    @Override
    public <U extends T> U get(Class<U> type, N name) {
        return get((Key<U, N>) cached.set(type, name));
    }

    @Override
    public <U extends T> U getOrDefault(Class<U> type, N name, U value) {
        return getOrDefault((Key<U, N>) cached.set(type, name), value);
    }

    
    @Override
    public Map<Key<? extends T, N>, T> map() {
        return this;
    }

    @Override
    public Object clone() {
        return super.clone();
    }
    
}


class TokenProxiedMap<T, N> implements TokenMap<T, N> {
    
    Map<Key<? extends T, N>, T> map;
    Key<T, N> cached;
    
    
    TokenProxiedMap(Map<Key<? extends T, N>, T> map) {
        this.map = map;
        cached = key(null, null);
    }
    
    
    @Override
    public <U extends T> boolean containsKey(Class<U> type, N name) {
        return containsKey(cached.set(type, name));
    }

    @Override
    public <U extends T> U get(Class<U> type, N name) {
        return get((Key<U, N>) cached.set(type, name));
    }

    @Override
    public <U extends T> U getOrDefault(Class<U> type, N name, U value) {
        return getOrDefault((Key<U, N>) cached.set(type, name), value);
    }

    
    @Override
    public Map<Key<? extends T, N>, T> map() {
        return map;
    }
    
}
