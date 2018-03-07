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


import org.junit.jupiter.api.*;

import static com.karuslabs.plugin.validator.validators.CommandValidator.KEYS;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static com.karuslabs.plugin.validator.Yaml.TEST;


@TestInstance(PER_CLASS)
class CommandValidatorTest {
    
    CommandValidator validator = new CommandValidator();
    
    
    @Test
    void executeConfiguration() {
        validator.validateConfigurationSection(TEST, "command");
    }
    
    
    @Test
    void validateCofigurationSection_ThrowsException() {
        assertEquals("Invalid keys: [invalid-key] at: .invalid-command, key must be valid: " + KEYS.keySet().toString(),
            assertThrows(ValidationException.class, () -> validator.validateConfigurationSection(TEST, "invalid-command")).getMessage()
        );
    }
    
}
