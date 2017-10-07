/*
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
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

import com.karuslabs.commons.annotation.JDK9;

import java.util.HashMap;

import org.bukkit.configuration.ConfigurationSection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.karuslabs.commons.configuration.Yaml.COMMANDS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class ElementTest {
    
    @JDK9("Diamond inference")
    private Element<String> element = spy(new Element<String>(new HashMap<>()) {
        @Override
        protected boolean check(ConfigurationSection config, String key) {
            return true;
        }

        @Override
        protected String handle(ConfigurationSection config, String key) {
            return "";
        }
        
    });
    
    
    @Test
    public void declare() {
        doReturn("value").when(element).parse(COMMANDS, "");
        
        element.declare(COMMANDS, "");
        
        assertEquals("value", element.getDeclarations().get(""));
        verify(element).parse(COMMANDS, "");
    }
    
    
    @ParameterizedTest
    @CsvSource({"key, 1, 0, 0", "commands.brush.translation, 0, 1, 0", "declare, 0, 0, 1"})
    public void parse(String key, int nullValue, int declaration, int handle) {
        doReturn("").when(element).handleNull(COMMANDS, key);
        doReturn("").when(element).getDeclared(COMMANDS, key);
        doReturn(true).when(element).check(COMMANDS, key);
        doReturn("").when(element).handle(COMMANDS, key);
        
        assertEquals("", element.parse(COMMANDS, key));
        verify(element, times(nullValue)).handleNull(COMMANDS, key);
        verify(element, times(declaration)).getDeclared(COMMANDS, key);
        verify(element, times(handle)).handle(COMMANDS, key);
    }
    
    
    @Test
    public void parse_ThrowsException() {
        doReturn(false).when(element).check(any(), any());
        
        assertEquals(
            "Invalid value for: " + COMMANDS.getCurrentPath() + ".",
            assertThrows(ParserException.class, () -> element.parse(COMMANDS, "")).getMessage()
        );
    }
    
    
    @Test
    public void handleNull() {
        assertEquals(
            "Missing key: .key",
            assertThrows(ParserException.class, () -> element.handleNull(COMMANDS, "key")).getMessage()
        );
    }
    
    
    @Test
    public void getDeclared() {
        element.getDeclarations().put("key", "value");
        assertEquals("value", element.getDeclared(COMMANDS, "key"));
    }
    
    
    @Test
    public void getDeclaration_ThrowsException() {
        assertEquals(
            "Missing declaration: declare.key",
            assertThrows(ParserException.class, () -> element.getDeclared(COMMANDS.getConfigurationSection("declare"), "key")).getMessage()
        );
        
    }
    
}
