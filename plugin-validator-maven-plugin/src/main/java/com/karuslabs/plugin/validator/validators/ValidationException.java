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
 * Thrown to indicate that an error has occurred while validating the document.
 */

public class ValidationException extends RuntimeException {
    
    /**
     * Constructs a {@code ValidationException} with the specified message.
     * 
     * @param message the message
     */
    public ValidationException(String message) {
        super(message);
    }
        
    /**
     * Constructs a {@code ValidationException} with the specified {@code ConfigurationSection}, key and expected type.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     * @param type the expected type
     */
    public ValidationException(ConfigurationSection config, String key, String type) {
        super("Invalid type for: " + config.getCurrentPath() + "." + key + ", value must be a " + type);
    }
    
}
