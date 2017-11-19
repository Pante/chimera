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
package com.karuslabs.commons.locale;

import com.karuslabs.commons.annotation.Immutable;

import java.util.*;
import java.util.concurrent.*;
import javax.annotation.Nullable;

import static java.util.Collections.EMPTY_LIST;
import static java.util.Collections.enumeration;


/**
 * A concrete, thread-safe subclass of {@code ResourceBundle} which manages resources for a locale using a backing {@code ConcurrentMap}.
 * <p>
 * The backing {@code ConcurrentMap} may only contain {@code String}s and {@code String[]}s.
 * <P>
 * Instances of this class should never be constructed directly, but rather through invoking {@link ResourceBundle#getBundle(String)} or 
 * any other overloaded variant.
 */
public class CachedResourceBundle extends ResourceBundle {
    
    /**
     * Represents an empty {@code CachedResourceBundle} which always returns the specified key
     * when {@code getString(String)} is invoked.
     */
    public static final CachedResourceBundle NONE = new CachedResourceBundle() {

        private final Enumeration<String> keys = enumeration(EMPTY_LIST);

        
        @Override
        protected Object handleGetObject(String key) {
            return key;
        }

        @Override
        public @Immutable Enumeration<String> getKeys() {
            return keys;
        }
        
    };

    
    
    private final ConcurrentMap<String, Object> messages;
    
    
    /**
     * Constructs an empty {@code CachedResourceBundle} backed by a {@code ConcurrentHashMap}.
     */
    public CachedResourceBundle() {
        this(new ConcurrentHashMap<>());
    }
    
    /**
     * Constructs a {@code CachedResourceBundle} backed by the specified {@code ConcurrentMap} 
     * which contains the keys and associated messages.
     * 
     * @param messages the backing map which contains the keys and associated messages
     */
    public CachedResourceBundle(ConcurrentMap<String, Object> messages) {
        this.messages = messages;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public @Immutable Enumeration<String> getKeys() {
        if (parent == null) {
            return enumeration(messages.keySet());
            
        } else {
            return new ResourceBundleEnumeration(parent.getKeys(), messages.keySet());
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected @Nullable Object handleGetObject(String key) {
        return messages.get(key);
    }
    
    
    /**
     * Returns the keys and associated messages for this {@code CachedResourceBundle}.
     * 
     * @return the keys and associated messages
     */
    public ConcurrentMap<String, Object> getMessages() {
        return messages;
    }
    
}
