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

import com.karuslabs.annotations.JDK9;
import com.karuslabs.commons.command.References;

import org.bukkit.configuration.ConfigurationSection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.karuslabs.commons.configuration.Yaml.COMMANDS;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;


class ReferableTokenTest {
    
    References references = new References();
    NullHandle handle = mock(NullHandle.class);
    
    @JDK9("Diamond reference...")
    ReferableToken<?> token = spy(new ReferableToken<Object>(references, handle) {
        @Override
        protected Object getReference(String key) {
            return key.equalsIgnoreCase("non-null") ? key : null;
        }

        @Override
        protected Object register(String key, Object reference) {
            return null;
        }

        @Override
        public boolean isAssignable(ConfigurationSection config, String key) {
            return key.equals("help.aliases");
        }

        @Override
        protected Object get(ConfigurationSection config, String key) {
            return null;
        } 

        @Override
        protected Object getDefaultReference() {
            return null;
        }
    });
    
    
    @ParameterizedTest
    @CsvSource({"non-null, 0, 0", "help.aliases, 1, 0", "other, 0, 1"})
    void getReference(String value, int registerTimes, int handleTimes) {
        token.getReference(COMMANDS, "a key", value);
        
        verify(token, times(registerTimes)).get(COMMANDS, value);
        verify(token, times(registerTimes)).register(value, null);
        verify(handle, times(handleTimes)).handle(COMMANDS, "a key", value);
    }
    
    
    @Test
    void getters() {
        assertSame(handle, token.getHandle());
        assertSame(references, token.getReferences());
    }
    
}
