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

import java.util.*;

import org.bukkit.configuration.ConfigurationSection;


/**
 * A concrete subclass of {@code ConfigurationValidator} which checks if a value is a {@code Command}.
 */
public class CommandValidator extends ConfigurationValidator {

    static final Map<String, Validator> KEYS = new HashMap<>(5);
    
    static {
        KEYS.put("description", STRING);
        KEYS.put("aliases", STRING_LIST);
        KEYS.put("permission", STRING);
        KEYS.put("permission-message", STRING);
        KEYS.put("usage", STRING);
    }
    
    
    /**
     * Checks if the value is a {@code Commnad}.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     */
    @Override
    protected void validateConfigurationSection(ConfigurationSection config, String key) {
        ConfigurationSection command = config.getConfigurationSection(key);
        Set<String> keys = command.getKeys(false);
        
        if (KEYS.keySet().containsAll(keys)) {
            keys.forEach(aKey -> KEYS.get(aKey).validate(command, aKey));
            
        } else {
            keys.removeAll(KEYS.keySet());
            throw new ValidationException("Invalid keys: " + keys.toString() + " at: " + config.getCurrentPath() + "." + key + ", key must be valid: " + KEYS.keySet().toString());
        }
    }
    
}
