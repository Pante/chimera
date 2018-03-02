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


public abstract class ReferableToken<T> extends Token<T> {
        
    protected References references;
    protected NullHandle handle;
    
    
    public ReferableToken(References references, NullHandle handle) {
        this.references = references;
        this.handle = handle;
    }

    
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
    
    protected abstract T getReference(String key);
    
    protected abstract T register(String key, T reference);
    
    protected abstract T getDefaultReference();
    
    
    public References getReferences() {
        return references;
    }
    
    public NullHandle getHandle() {
        return handle;
    }
    
}
