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


public class PermissionValidator extends ConfigurationValidator {
    
    static final Map<String, Validator> KEYS = new HashMap<>();
    
    static {
        KEYS.put("description", STRING);
        KEYS.put("default", BOOLEAN);
        KEYS.put("children", NONE);
    }

    
    @Override    
    protected void validateConfigurationSection(ConfigurationSection config, String key) {
        ConfigurationSection permission = config.getConfigurationSection(key);
        Set<String> keys = permission.getKeys(false);

        if (!KEYS.keySet().containsAll(keys)) {
            throw new ValidationException("Invalid keys: " + keys.toString() + " at: " + config.getCurrentPath() + "." + key + ", key must be valid: " + KEYS.keySet().toString());
        }

        if (keys.contains("children")) {
            validateChildren(config.getKeys(false), permission, "children");
        }
        
        keys.forEach(aKey -> {
            KEYS.get(aKey).validate(permission, aKey);
        });
    }
    
    @Override
    protected void orElse(ConfigurationSection config, String key) {
        BOOLEAN.validate(config, key);
    }
    
    protected void validateChildren(Set<String> permissions, ConfigurationSection config, String key) {
        if (!config.isConfigurationSection(key)) {
            throw new ValidationException(config, key, "ConfigurationSection");
        }
        
        ConfigurationSection children = config.getConfigurationSection(key);
        children.getKeys(false).forEach(child -> {
            if (!permissions.contains(child)) {
                throw new ValidationException("Unresolvable permission: " + child + " at: " + children.getCurrentPath() + "." + child + ", permission must be declared");
                
            } else {
                BOOLEAN.validate(children, child);
            }
        });
    }
    
}
