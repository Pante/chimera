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
package com.karuslabs.commons.command.parser;

import org.bukkit.configuration.ConfigurationSection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.karuslabs.commons.configuration.Yaml.COMMANDS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class TokenTest {
    
    Token<?> token = spy(new Token<Object>() {
        
        @Override
        public boolean isAssignable(ConfigurationSection config, String key) {
            return key.equals("help.aliases");
        }

        @Override
        protected Object get(ConfigurationSection config, String key) {
            return null;
        }
    });
    
    
    @Test
    void from() {
        token.from(COMMANDS, "help.aliases");
        
        verify(token).from(COMMANDS, "help.aliases");
    }
    
    
    @ParameterizedTest
    @CsvSource({
        "idk, 'Missing key: idk, key cannot be non-existent'", 
        "help.usage, 'Illegal reference: usage at: .help.usage, reference not allowed here'",
        "help.translation, 'Invalid value for key: .help.translation'"
    })
    void from_ThrowsException(String key, String message) {
        assertEquals(
            message, 
            assertThrows(ParserException.class, () -> token.from(COMMANDS, key)).getMessage()
        );
    }
    
}
