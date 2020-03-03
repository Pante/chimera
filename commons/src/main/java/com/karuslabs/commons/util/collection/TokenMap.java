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
package com.karuslabs.commons.util.collection;

import com.google.common.primitives.Primitives;

import com.karuslabs.annotations.ValueType;
import com.karuslabs.commons.util.collection.TokenMap.Key;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;

import static com.karuslabs.commons.util.collection.TokenMap.key;


public interface TokenMap<N, T> {
    
    public static <N, T> TokenMap<N, T> of() {
        return new HashTokenMap<>();
    }
    
    public static <N, T> TokenMap<N, T> of(int capacity) {
        return new HashTokenMap<>(capacity);
    }
    
    public static <N, T> TokenMap<N, T> of(Map<Key<N, ? extends T>, T> map) {
        return new ProxiedTokenMap<>(map);
    }
    
    
    public <U extends T> boolean containsKey(N name, Class<U> type);
    
    public default <U extends T> boolean containsKey(Key<N, U> key) {
        return map().containsKey(key);
    }
    
    public default <U extends T> boolean containsValue(U value) {
        return map().containsValue(value);
    }
    
    
    public <U extends T> @Nullable U get(N name, Class<U> type);
    
    public default <U extends T> @Nullable U get(Key<N, U> key) {
        return (U) map().get(key);
    }
    
    
    public <U extends T> U getOrDefault(N name, Class<U> type, U value);
    
    public default <U extends T> U getOrDefault(Key<N, U> key, U value) {
        T item = map().get(key);
        if (item != null && Primitives.wrap(key.type).isAssignableFrom(item.getClass())) {
            return (U) item;
        } else {
            return value;
        }
    }
    
    
    public default <U extends T> @Nullable U put(N name, Class<U> type, U value) {
        return put(key(name, type), value);
    }
    
    public default <U extends T> @Nullable U put(Key<N, U> key, U value) {
        return (U) map().put(key, value);
    }
    
    
    public <U extends T> @Nullable U remove(N name, Class<U> type);
    
    public default <U extends T> @Nullable U remove(Key<N, U> key) {
        return (U) map().remove(key);
    }
    
    
    public Map<Key<N, ? extends T>, T> map();
    
    
    public static <N, T> Key<N, T> key(N name, Class<T> type) {
        return new Key<>(name, type);
    }
    
    public @ValueType final class Key<N, T> {

        N name;
        Class<? extends T> type;
        int hash;

        Key(N name, Class<T> type) {
            this.name = name;
            this.type = type;
            this.hash = hash();
        }

        Key<N, ? extends T> set(N name, Class<? extends T> type) {
            this.type = type;
            this.name = name;
            this.hash = hash();
            return this;
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
        public String toString() {
            return "Key[name: " + name + " class: " + type.getName() + "]";
        }

    }
    
}


class HashTokenMap<N, T> extends HashMap<Key<N, ? extends T>, T> implements TokenMap<N, T> {
    
    Key<N, T> cached;
    
    
    HashTokenMap() {
        cached = key(null, null);
    }
    
    HashTokenMap(int capacity) {
        super(capacity);
        cached = key(null, null);
    }
    
    
    @Override
    public <U extends T> boolean containsKey(N name, Class<U> type) {
        return containsKey(cached.set(name, type));
    }

    @Override
    public <U extends T> @Nullable U get(N name, Class<U> type) {
        return get((Key<N, U>) cached.set(name, type));
    }

    @Override
    public <U extends T> U getOrDefault(N name, Class<U> type, U value) {
        return getOrDefault((Key<N, U>) cached.set(name, type), value);
    }
    
    @Override
    public <U extends T> @Nullable U remove(N name, Class<U> type) {
        return remove((Key<N, U>) cached.set(name, type));
    }

    
    @Override
    public Map<Key<N, ? extends T>, T> map() {
        return this;
    }
    
}


class ProxiedTokenMap<N, T> implements TokenMap<N, T> {
    
    Map<Key<N, ? extends T>, T> map;
    Key<N, T> cached;
    
    
    ProxiedTokenMap(Map<Key<N, ? extends T>, T> map) {
        this.map = map;
        cached = key(null, null);
    }
    
    
    @Override
    public <U extends T> boolean containsKey(N name, Class<U> type) {
        return containsKey(cached.set(name, type));
    }

    @Override
    public <U extends T> @Nullable U get(N name, Class<U> type) {
        return get((Key<N, U>) cached.set(name, type));
    }

    @Override
    public <U extends T> U getOrDefault(N name, Class<U> type, U value) {
        return getOrDefault((Key<N, U>) cached.set(name, type), value);
    }
    
    @Override
    public <U extends T> @Nullable U remove(N name, Class<U> type) {
        return remove((Key<N, U>) cached.set(name, type));
    }

    
    @Override
    public Map<Key<N, ? extends T>, T> map() {
        return map;
    }
    
}
