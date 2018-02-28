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
package com.karuslabs.spigot.plugin.processor.processors;

import com.karuslabs.spigot.plugin.processor.ProcessorException;

import org.bukkit.configuration.ConfigurationSection;


@FunctionalInterface
public interface Processor {
    
    public void execute(ConfigurationSection config, String key);
    
    
    public static final Processor BOOLEAN = (config, key) -> {
        if (!config.isBoolean(key)) {
            throw new ProcessorException(config, key, "boolean");
        }
    };
    
    public static final Processor STRING = (config, key) -> {
        if (!config.isString(key)) {
            throw new ProcessorException(config, key, "String");
        }
    };
    
    public static final Processor STRING_LIST = (config, key) -> {
        if (!config.isList(key)) {
            throw new ProcessorException(config, key, "List of Strings");
        }
    };
    
    public static final Processor NONE = (config, key) -> {};
    
}
