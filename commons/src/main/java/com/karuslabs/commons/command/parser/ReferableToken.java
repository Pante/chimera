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
package com.karuslabs.commons.command.parser;

import com.karuslabs.commons.command.References;

import javax.annotation.Nullable;

import org.bukkit.configuration.ConfigurationSection;


/**
 * A subclass of {@code Token} which may hold references to other values of the same type.
 * 
 * @param <T> the type of the value
 */
public abstract class ReferableToken<T> extends Token<T> {
    
    /**
     * References which were registered or resolved and cached.
     */
    protected References references;
    /**
     * Handle for unresolvable references.
     */
    protected NullHandle handle;
    
    
    /**
     * Constructs a {@code ReferableToken} with the specified references and {@code NullHandle}.
     * 
     * @param references the references
     * @param handle the NullHandle
     */
    public ReferableToken(References references, NullHandle handle) {
        this.references = references;
        this.handle = handle;
    }

    
    /**
     * Returns the reference associated with the value in the specified key if the reference is registered or cached;
     * else if the reference is resolvable, invoke and return {@link #register(String, Object)};
     * else invoke {@link NullHandle#handle(ConfigurationSection, String, String)} and return the value
     * specified by {@link #getDefaultReference()}.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     * @param value the value
     * @return the reference
     */
    @Override
    protected @Nullable T getReference(ConfigurationSection config, String key, String value) {
        T reference = getReference(value);
        if (reference != null) {
            return reference;
            
        } else if (isAssignable(config.getRoot(), value)) {
            return register(value, get(config.getRoot(), value));
            
        } else {
            handle.handle(config, key, value);
            return getDefaultReference();
        }
    }
    
    /**
     * Returns the reference associated with the specified key.
     * 
     * @param key the key
     * @return the reference
     */
    protected abstract T getReference(String key);
    
    /**
     * Associates the reference to the specified key and registers to the {@code References}.
     * 
     * @param key the key
     * @param reference the reference
     * @return the reference
     */
    protected abstract T register(String key, T reference);
    
    /**
     * Returns the default value for unresolvable references.
     * 
     * @return the default value
     */
    protected abstract T getDefaultReference();
    
    
    /**
     * Returns the {@code References}.
     * 
     * @return the references which were registered or resolved and cached
     */
    public References getReferences() {
        return references;
    }
    
    /**
     * Returns the {@code NullHandle}.
     * 
     * @return the handle for unresolvable references
     */
    public NullHandle getHandle() {
        return handle;
    }
    
}
