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

import com.karuslabs.plugin.validator.ValidationException;

import java.util.*;

import org.bukkit.configuration.ConfigurationSection;

import static java.util.Arrays.asList;


/**
 * A {@code Validator} implementation which checks if a value is an enumeration value.
 */
public class EnumerationValidator implements Validator {

    Set<String> values;
    
    
    /**
     * Constructs a {@code EnumerationValidator} with the specified enumeration values.
     * 
     * @param values the values
     */
    public EnumerationValidator(String... values) {
        this.values = new HashSet<>(asList(values));
    }
    
    /**
     * Checks if the value is an enumeration value.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     * @throws ValidationException if the value is not a enumeration value
     */
    @Override
    public void validate(ConfigurationSection config, String key) {
        if (!values.contains(config.getString(key))) {
            throw new ValidationException("Invalid value: " + config.getString(key) + " at: " + config.getCurrentPath() + "." + key + ", value must be valid: " + values.toString());
        }
    }
    
}
