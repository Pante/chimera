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

import org.bukkit.configuration.ConfigurationSection;


/**
 * A {@code Processor} implementation which processes the name.
 */
public class NameProcessor implements Processor {
    
    private String name;
    private boolean override;
    
    
    /**
     * Constructs a {@code NameProcessor} with the specified name and override.
     * 
     * @param name the name
     * @param override the override
     */
    public NameProcessor(String name, boolean override) {
        this.name = name;
        this.override = override;
    }
    
    
    /**
     * Sets the plugin name to the name if the name does not exist or overriding is allowed.
     * 
     * @param config the config
     * @param key the key
     */
    @Override
    public void execute(ConfigurationSection config, String key) {
        if (config.getString(key) == null || override) {
            config.set(key, name);
        }
    }
    
}
