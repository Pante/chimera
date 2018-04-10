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


public abstract class Token<T> {
    
    public T from(ConfigurationSection config, String key) {
        if (config.get(key) == null) {
            return getNull(config, key);
            
        } else if (isAssignable(config, key)) {
            return get(config, key);
            
        } else if (config.isString(key)) {            
            return getReference(config, key, config.getString(key));
            
        } else {
            throw new ParserException("Invalid value for key: " + config.getCurrentPath() + "." + key);
        }
    }
    
    protected T getNull(ConfigurationSection config, String key) {
        throw new ParserException("Missing key: " + config.getCurrentPath() + key + ", key cannot be non-existent");
    }
    
    protected T getReference(ConfigurationSection config, String key, String value) {
        throw new ParserException("Illegal reference: " + value + " at: " + config.getCurrentPath() + "." + key + ", reference not allowed here");
    }
    
    public abstract boolean isAssignable(ConfigurationSection config, String key);
    
    protected abstract T get(ConfigurationSection config, String key);
    
}
