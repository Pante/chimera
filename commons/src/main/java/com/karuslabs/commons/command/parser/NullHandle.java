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

import org.bukkit.configuration.ConfigurationSection;


/**
 * Represents a handle for {@code null} references.
 */
@FunctionalInterface
public interface NullHandle {
    
    /**
     * A {@code NullHandle} which throws a {@code ParserException}.
     */
    public NullHandle EXCEPTION = (config, key, value) -> {
        throw new ParserException("Invalid reference: \"" + value + "\" at: \"" + config.getCurrentPath() + "." + key + "\", reference must either be registered or point to a assignable value");
    };
    
    /**
     * An empty {@code NullHandle} which does nothing.
     */
    public NullHandle NONE = (config, key, value) -> {};
    
    
    /**
     * Handles a {@code null} reference.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     * @param value the value
     */
    public void handle(ConfigurationSection config, String key, String value);
    
}
