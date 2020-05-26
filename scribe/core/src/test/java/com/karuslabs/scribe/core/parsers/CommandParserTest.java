/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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
package com.karuslabs.scribe.core.parsers;

import com.karuslabs.scribe.annotations.Command;
import com.karuslabs.scribe.core.*;
import com.karuslabs.scribe.core.parsers.CommandParser.Label;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


@Command(name = "name", aliases = {"a", "b"}, description = "desc", syntax = "syntax", permission = "a.b", message = "message")
class CommandParserTest {
    
    @ParameterizedTest
    @MethodSource("values")
    void values(Label label, String expected) {
        assertEquals(expected, label.toString());
    }
    
    static Stream<Arguments> values() {
        return Stream.of(
            of(Label.NAME, "name"),
            of(Label.ALIAS, "alias")
        );
    }
    
    
    Environment<Class<?>> environment = StubEnvironment.of(Project.EMPTY);
    CommandParser<Class<?>> parser = new CommandParser<>(environment);
    Command command = CommandParserTest.class.getAnnotation(Command.class);

    
    
    @Test
    void parse_class() {
        parser.parse(Set.of(CommandParserTest.class));
        
        var mapping = environment.mappings;
        
        var commands = (Map<String, Object>) mapping.get("commands");
        var command = (Map<String, Object>) commands.get("name");
        
        verify(environment, times(0)).error(any(), any());
        verify(environment, times(0)).warn(any(), any());
        
        assertEquals(1, mapping.size());
        assertEquals(1, commands.size());
        
        assertEquals(5, command.size());
        assertArrayEquals(new String[] {"a", "b"}, (String[]) command.get("aliases"));
        assertEquals("desc", command.get("description"));
        assertEquals("syntax", command.get("usage"));
        assertEquals("a.b", command.get("permission"));
        assertEquals("message", command.get("permission-message"));
    }
    
    
    @Test
    void check_whitespaced_name() {
        parser.check(Object.class, command, "invalid name", Label.NAME);
        
        assertTrue(parser.names.isEmpty());
        verify(environment).error(Object.class, "\"invalid name\" is not a valid command name, should not contain whitespaces");
    }
    
    
    @Test
    void check_empty_name() {
        parser.check(Object.class, command, "", Label.NAME);
        
        assertTrue(parser.names.isEmpty());
        verify(environment).error(Object.class, "Command name should not be empty");
    }
    
    
    @Test
    void check_adds_name() {
        parser.check(Object.class, command, "alias", Label.ALIAS);
        var entry = parser.names.get("alias");
        
        verify(environment, times(0)).error(any(), any());
        verify(environment, times(0)).warn(any(), any());
        
        assertEquals(command, entry.getKey());
        assertEquals(Label.ALIAS, entry.getValue());
    }
    
    
    @Test
    void check_existing_namespace() {
        parser.names.put("something", new SimpleEntry<>(command, Label.ALIAS));
        
        parser.check(Object.class, command, "something", Label.NAME);
        
        verify(environment).error(Object.class, "\"something\" already exists, commands should not have the same aliases and names");
    }
    
    
    @Test
    void parse_command() {
        var results = parser.parse(Object.class, command);
        
        assertTrue(parser.names.isEmpty());
        
        assertEquals(5, results.size());
        assertArrayEquals(new String[] {"a", "b"}, (String[]) results.get("aliases"));
        assertEquals("desc", results.get("description"));
        assertEquals("syntax", results.get("usage"));
        assertEquals("a.b", results.get("permission"));
        assertEquals("message", results.get("permission-message"));
    }
    
    
    @Test
    void parse_command_warn() {
        var results = parser.parse(Object.class, MalformedPermission.class.getAnnotation(Command.class));
        
        verify(environment).warn(Object.class, "\"a?s\" may be malformed");
        assertEquals(2, results.size()); 
    }
    
    
    @Command(name = "otherwise", permission = "a?s")
    static class MalformedPermission {
    
    }
    
    
    @Test
    void clear() {
        parser.names.put("name", new AbstractMap.SimpleEntry<>(null, null));
        
        parser.clear();
        
        assertTrue(parser.names.isEmpty());
    }

} 
