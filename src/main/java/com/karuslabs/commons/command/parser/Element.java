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
package com.karuslabs.commons.command.parser;

import java.util.*;

import org.bukkit.configuration.ConfigurationSection;


public abstract class Element<T> {    
    
    protected Map<String, T> definitions;
    
    
    public Element(Map<String, T> definitions) {
        this.definitions = definitions;
    }
    
    
    public void define(String key, Object value) {
        definitions.put(key, parse(value));
    }
    
    public T parse(Object value) {
        if (value instanceof String && definitions.containsKey(value)) {
            return definitions.get(value);
            
        } else if (value instanceof ConfigurationSection) {
            return parseConfigurationSection((ConfigurationSection) value);
            
        } else {
            throw new IllegalArgumentException("Failed to parse token: " + value);
        }
    }
    
    protected T parseConfigurationSection(ConfigurationSection config) {
        throw new IllegalArgumentException("Failed to parse token: " + config.getName());
    }
    
    
    public Map<String, T> getDefinitions() {
        return definitions;
    }
    
}
