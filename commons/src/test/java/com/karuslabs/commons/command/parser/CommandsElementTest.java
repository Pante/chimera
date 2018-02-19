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
import com.karuslabs.commons.command.Command;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.karuslabs.commons.configuration.Yaml.*;
import static java.util.Arrays.asList;
import static java.util.Collections.EMPTY_MAP;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import static org.mockito.Mockito.*;


@TestInstance(PER_CLASS)
class CommandsElementTest {
    
    CommandsElement element;
    CommandElement command;
    
    
    CommandsElementTest() {
        element = new CommandsElement(null);
        command = when(mock(CommandElement.class).handleDeclared(any(), any())).thenReturn(mock(Command.class)).getMock();
        when(command.parse(any(), any())).thenReturn(mock(Command.class));
        element.setCommandElement(command);
    }
    
    
    @Test
    void handleNull() {
        assertEquals(EMPTY_MAP, element.handleNull(null, null));
    }
    
    
    @ParameterizedTest
    @CsvSource({"true, declare", "false, declare.commands.help.aliases"})
    void check(boolean expected, String key) {
        assertEquals(expected, element.check(COMMANDS, key));
    }
    
    
    @Test
    void handle() {
        @JDK9("Set.of(...)")
        Set<String> names = new HashSet<>(asList("help", "fill"));
                
        String path = "commands.brush.subcommands";
        assertEquals(names, element.handle(COMMANDS, path).keySet());
        verify(command).handleDeclared(COMMANDS.getConfigurationSection(path), "help");
        verify(command).parse(COMMANDS.getConfigurationSection(path), "fill");
    }
    
}