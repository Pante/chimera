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
package com.karuslabs.plugin.validator.validators;

import org.bukkit.configuration.ConfigurationSection;


/**
 * Represents a {@code Validator} implementation which checks if a value in the 
 * {@code ConfigurationSection} is a {@code ConfigurationSection}.
 */
public abstract class ConfigurationValidator implements Validator {
    
    /**
     * Checks if the value associated with the specified key is a {@code ConfigurationSection}.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     */
    @Override
    public void validate(ConfigurationSection config, String key) {
        if (config.isConfigurationSection(key)) {
            validateConfigurationSection(config, key);
            
        } else {
            orElse(config, key);
        }
    }
    
    /**
     * Checks if the values in the {@code ConfigurationSection} are valid.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     */
    protected abstract void validateConfigurationSection(ConfigurationSection config, String key);
    
    /**
     * Handles a value which is not a {@code ConfigurationSection}.
     * 
     * The default implementation throws a {@code ProcessorException}.
     * 
     * Subclasses should override this method to customise the handling of values which are not {@code ConfigurationSection}s.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     * @throws ValidationException of this method is not overriden
     */
    protected void orElse(ConfigurationSection config, String key) {
        throw new ValidationException(config, key, "ConfigurationSection");
    }
    
}
