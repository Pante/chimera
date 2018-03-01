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
import com.karuslabs.commons.command.completion.*;

import org.bukkit.configuration.ConfigurationSection;


/**
 * A concrete subclass of {@code Token} which creates {@code Completion}s.
 */
public class CompletionToken extends ReferableToken<Completion> {
    
    /**
     * Constructs a {@code CompletionElement} with the specified references and {@code NullHandle}.
     * 
     * @param references the references
     * @param handle the NullHandle
     */
    public CompletionToken(References references, NullHandle handle) {
        super(references, handle);
    }

    
    /**
     * Returns the {@code Completion} associated with the specified key in the {@code ConfigurationSection}.
     * 
     * @param key the key
     * @return the value associated with the specified key
     */
    @Override
    protected Completion getReference(String key) {
        return references.getCompletion(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Completion register(String key, Completion reference) {
        references.completion(key, reference);
        return reference;
    }
    
    /**
     * Checks if the specified key in the {@code ConfigurationSection} is a list.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     * @return true if the value associated with the specified key in the ConfigurationSection is a list; else false
     */
    @Override
    public boolean isAssignable(ConfigurationSection config, String key) {
        return config.isList(key);
    }
    
    /**
     * Creates a {@code CachedCompletion} for the specified key in the {@code ConfigurationSection}.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     * @return a CachedCompletion
     */
    @Override
    protected Completion get(ConfigurationSection config, String key) {
        return new CachedCompletion(config.getStringList(key));
    }
    
    /**
     * Returns {@link Completion#NONE}.
     * 
     * @return the Completion
     */
    @Override
    protected Completion getDefaultReference() {
        return Completion.NONE;
    }
    
}
