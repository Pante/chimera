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
 * Represent a key-value pair in an YAMl document which produces a value.
 * 
 * @param <T> the type of the value
 */
public abstract class Token<T> {
    
    /**
     * Parses the specified key in the {@code ConfigurationSection} and produces a value.
     * 
     * The default implementation invokes {@link #getNull(ConfigurationSection, String)} if the value is {@code null};
     * else if {@link #isAssignable(ConfigurationSection, String)} returns {@code true}, invokes {@link #get(ConfigurationSection, String)};
     * else if the key is a {@code String}, invokes {@link #getReference(ConfigurationSection, String, String)};
     * else throws a {@code ParserException}.
     * 
     * @param config the ConfigurationSection which contains the specified key-value pair
     * @param key the key
     * @return the value
     * @throws ParserException if this method fails to produce a derived value for the specified ConfigurationSection and key
     */
    public T from(ConfigurationSection config, String key) {
        if (config.get(key) == null) {
            return getNull(config, key);
            
        } else if (isAssignable(config, key)) {
            return get(config, key);
            
        } else if (config.isString(key)) {            
            return getReference(config, key, config.getString(key));
            
        } else {
            throw new ParserException("Invalid value for key: " + config.getCurrentPath() + key);
        }
    }
    
    /**
     * Returns a default value if the specified key in the {@code ConfigurationSection} is {@code null}.
     * This method is invoked by {@link #from(ConfigurationSection, String)}. 
     * 
     * The default implementation throws a {@code ParserException}. 
     * 
     * Subclasses may override this method to customise the handling of {@code null} values.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     * @return a default value
     * @throws ParserException if this method is invoked but not overridden
     */
    protected T getNull(ConfigurationSection config, String key) {
        throw new ParserException("Missing key: " + config.getCurrentPath() + key + ", key cannot be non-existent");
    }
    
    /**
     * Returns the value associated with the specified key, declared beforehand.
     * This method is invoked by {@link #from(ConfigurationSection, String)}.

     * The default implementation throws a {@code ParserException}.

     * Subclasses may override this method to customise the handling of references.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     * @param value the value
     * @return the value
     * @throws ParserException if this method is invoked but not overridden
     */
    protected T getReference(ConfigurationSection config, String key, String value) {
        throw new ParserException("Illegal reference: " + value + " at: " + config.getCurrentPath() + "." + key + ", reference not allowed here");
    }
    
    /**
     * Checks if the value to return is derivable from the specified key in the {@code ConfigurationSection}.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     * @return true if the specified key is valid; else false
     */
    public abstract boolean isAssignable(ConfigurationSection config, String key);
    
    /**
     * Creates a value for the specified key in the {@code ConfigurationSection}.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     * @return the value
     */
    protected abstract T get(ConfigurationSection config, String key);
    
}
