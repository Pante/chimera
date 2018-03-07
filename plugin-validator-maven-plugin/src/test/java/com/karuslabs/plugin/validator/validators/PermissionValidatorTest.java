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

import static java.util.Collections.EMPTY_SET;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static com.karuslabs.plugin.validator.Yaml.TEST;


@TestInstance(PER_CLASS)
class PermissionValidatorTest {
    
    PermissionValidator validator = new PermissionValidator();
    
    
    @Test
    void execute() {
        validator.validate(TEST.getConfigurationSection("permissions"), "a");
    }
    
    
    @Test
    void orElse() {
        validator.orElse(TEST, "permissions.b");
    }
    
    
    @Test
    void validateConfigurationSection_Key_ThrowsException() {
        assertEquals("Invalid keys: [invalid-key] at: .invalid-permission-key, key must be valid: " + PermissionValidator.KEYS.keySet().toString(),
            assertThrows(ValidationException.class, () -> validator.validateConfigurationSection(TEST, "invalid-permission-key")).getMessage()
        );
    }
    
    
    @Test
    void validateChildren_ConfigurationSection_ThrowsException() {
        assertEquals("Invalid type for: .invalid-children, value must be a ConfigurationSection",
            assertThrows(ValidationException.class, () -> validator.validateChildren(EMPTY_SET, TEST, "invalid-children")).getMessage()
        );
    }
    
    
    @Test
    void validateChildren_Reference_ThrowsException() {
        assertEquals("Unresolvable permission: unresolvable at: unresolvable-children.unresolvable, permission must be declared",
            assertThrows(ValidationException.class, () -> validator.validateChildren(EMPTY_SET, TEST, "unresolvable-children")).getMessage()
        );
    }
    
}
