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

import org.bukkit.configuration.ConfigurationSection;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.karuslabs.plugin.validator.Yaml.TEST;


class ConfigurationValidatorTest {
    
    ConfigurationValidator validator = spy(new ConfigurationValidator() {
        @Override
        protected void validateConfigurationSection(ConfigurationSection config, String key) {
            
        }
    });
    
    
    @Test
    void orElse() {
        doNothing().when(validator).orElse(TEST, "non-configuration-section");
        
        validator.validate(TEST, "non-configuration-section");
        
        verify(validator).validate(TEST, "non-configuration-section");
    }

    
    @Test
    void orElse_ThrowsException() {
        assertEquals("Invalid type for: .non-configuration-section, value must be a ConfigurationSection",
            assertThrows(ValidationException.class, () -> validator.orElse(TEST, "non-configuration-section")).getMessage()
        );
    }
    
}
