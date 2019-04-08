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

import com.google.common.primitives.Primitives;

import com.karuslabs.annotations.ValueBased;
import com.karuslabs.commons.util.collections.TokenMap.Key;

import java.util.*;

import static com.karuslabs.commons.util.collections.TokenMap.key;


/**
 * A map that associates a value with a key. Each key contains a name and the class 
 * of the value associated with the key. The respective primitive and boxed types
 * are considered distinct. Thus different values can be mapped to a primitive 
 * and its boxed class.
 * 
 * @param <N> the type of the names of keys
 * @param <T> the type of the values
 */
public interface TokenMap<N, T> {
    
    /**
     * Creates a {@code TokenMap}.
     * 
     * @param <N> the type of the keys
     * @param <T> the type of the values
     * @return a {@code TokenMap}
     */
    public static <N, T> TokenMap<N, T> of() {
        return new HashTokenMap<>();
    }
    
    /**
     * Creates a {@code TokenMap} with the given initial capacity.
     * 
     * @param <N> the type of the keys
     * @param <T> the type of the values
     * @param capacity the initial capacity
     * @return a {@code TokenMap}
     */
    public static <N, T> TokenMap<N, T> of(int capacity) {
        return new HashTokenMap<>(capacity);
    }
    
    /**
     * Creates a {@code TokenMap} backed by the given map.
     * 
     * @param <N> the type of the keys
     * @param <T> the type of the values
     * @param map the backing map
     * @return a {@code TokenMap}
     */
    public static <N, T> TokenMap<N, T> of(Map<Key<N, ? extends T>, T> map) {
        return new ProxiedTokenMap<>(map);
    }
    
    
    /**
     * Returns {@code true} if this map contains a mapping for the given name and 
     * type.
     * 
     * @param <U> the type
     * @param name the name of the key
     * @param type the type
     * @return {@code true} if this map contains a mapping for the given name and 
     *         type
     */
    public <U extends T> boolean containsKey(N name, Class<U> type);
    
    /**
     * Returns {@code true} if this map contains a mapping for the given key.
     * 
     * @param <U> the type
     * @param key the key
     * @return {@code true} if this map contains a mapping for the given key
     */
    public default <U extends T> boolean containsKey(Key<N, U> key) {
        return map().containsKey(key);
    }
    
    /**
     * Returns {@code true} if this map contains the given value.
     * 
     * @param <U> the type of the value
     * @param value the value
     * @return {@code true} if this map contains the value
     */
    public default <U extends T> boolean containsValue(U value) {
        return map().containsValue(value);
    }
    
    
    /**
     * Returns the value to which the given name and type are mapped, or {@code null} 
     * if this map contains no mapping for the name and type.
     * 
     * @param <U> the type of the value
     * @param name the name of the key
     * @param type the type
     * @return the value associated with the given name and type, or {@code null} 
     *         if this map contains no mapping for the name and type
     */
    public <U extends T> U get(N name, Class<U> type);
    
    /**
     * Returns the value to which the given key is mapped, or {@code null} if this
     * map contains no mapping for the key.
     * 
     * @param <U> the type of the value
     * @param key the key
     * @return the value associated with the given key, or {@code null} if this 
     *         map contains no mapping for the key
     */
    public default <U extends T> U get(Key<N, U> key) {
        return (U) map().get(key);
    }
    
    
    /**
     * Returns the value to which the given name and type are mapped, or {@code defaultValue}
     * if the map contains no mapping for the name and type.
     * 
     * @param <U> the type of the value
     * @param name the name of the key
     * @param type the type
     * @param value the default value
     * @return the value to which the given name and type are mapped, or {@code defaultValue} 
     *         if this map contains no mapping for the name and type
     */
    public <U extends T> U getOrDefault(N name, Class<U> type, U value);
    
    /**
     * Returns the value to which the given key is mapped, or {@code defaultValue}
     * if the map contains no mapping for the key.
     * 
     * @param <U> the type of the value
     * @param key the key
     * @param value the default value
     * @return the value to which the given key is mapped, or {@code defaultValue}
     *         if this map contains no mapping for the key
     */
    public default <U extends T> U getOrDefault(Key<N, U> key, U value) {
        T item = map().get(key);
        if (item != null && Primitives.wrap(key.type).isAssignableFrom(item.getClass())) {
            return (U) item;
        } else {
            return value;
        }
    }
    
    
    /**
     * Associates the given value with the given name and type. If this map previously 
     * contained a mapping for the name and type, the old value is replaced and 
     * returned.
     * 
     * @param <U> the type of the value
     * @param name the name of the key with which the value is associated
     * @param type the type of the key with which the value is to be associated
     * @param value the value to be associated with the type
     * @return the previous value associated with the name and type, or {@code null}
     *         if there was no mapping for the type
     */
    public default <U extends T> U put(N name, Class<U> type, U value) {
        return put(key(name, type), value);
    }
    
    /**
     * Associates the given value with the given key. If this map previously contained
     * a mapping for the key, the old value is replaced and returned.
     * 
     * @param <U> the type of the value
     * @param key the key with which the value is to be associated
     * @param value the value to be associated with the type
     * @return the previous value associated with the key, or {@code null} if there 
     *         was no mapping for the key
     */
    public default <U extends T> U put(Key<N, U> key, U value) {
        return (U) map().put(key, value);
    }
    
    
    /**
     * Removes the mapping for the name and type from this map if present. Returns 
     * the value to which this map previously associated the name and type, or 
     * {@code null} if the map contains no mapping for the name and type.
     * 
     * @param <U> the type of the value
     * @param name the name of the key
     * @param type the type of the key
     * @return the previous value associated with the name and type, or {@code null}
     *         if there was no mapping for the name and type
     */
    public <U extends T> U remove(N name, Class<U> type);
    
    /**
     * Removes the mapping for the key from this map if present. Returns the value
     * to which this map previously associated the key, or {@code null} if the map 
     * contains no mapping for the key.
     * 
     * @param <U> the type of the value
     * @param key the key whose mapping is to be removed
     * @return the previous value associated with the key, or {@code null} if there 
     *         was no mapping for the key
     */
    public default <U extends T> U remove(Key<N, U> key) {
        return (U) map().remove(key);
    }
    
    
    /**
     * Returns a {@code Map} view of this {@code TokenMap}. Modifications to the
     * map view will affect this {@code TokenMap}.
     * 
     * @return a {@code Map} view
     */
    public Map<Key<N, ? extends T>, T> map();
    
    
    /**
     * Creates a {@code Key} with the given name and type.
     * 
     * @param <N> the type of the name
     * @param <T> the type of the key
     * @param name the name of the key
     * @param type the type of the key
     * @return a {@code Key}
     */
    public static <N, T> Key<N, T> key(N name, Class<T> type) {
        return new Key<>(name, type);
    }
    
    /**
     * A {@code Key} to which a value is associated in a {@code TokenMap}.
     * 
     * @param <N> the type of the name
     * @param <T> the typ of the key
     */
    public @ValueBased final class Key<N, T> {

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
    public <U extends T> U get(N name, Class<U> type) {
        return get((Key<N, U>) cached.set(name, type));
    }

    @Override
    public <U extends T> U getOrDefault(N name, Class<U> type, U value) {
        return getOrDefault((Key<N, U>) cached.set(name, type), value);
    }
    
    @Override
    public <U extends T> U remove(N name, Class<U> type) {
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
    public <U extends T> U get(N name, Class<U> type) {
        return get((Key<N, U>) cached.set(name, type));
    }

    @Override
    public <U extends T> U getOrDefault(N name, Class<U> type, U value) {
        return getOrDefault((Key<N, U>) cached.set(name, type), value);
    }
    
    @Override
    public <U extends T> U remove(N name, Class<U> type) {
        return remove((Key<N, U>) cached.set(name, type));
    }

    
    @Override
    public Map<Key<N, ? extends T>, T> map() {
        return map;
    }
    
}
