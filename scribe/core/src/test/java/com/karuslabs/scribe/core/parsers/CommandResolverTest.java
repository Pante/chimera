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

import com.karuslabs.scribe.core.parsers.CommandParser;
import com.karuslabs.scribe.maven.plugin.Message;
import com.karuslabs.scribe.annotations.Command;
import com.karuslabs.scribe.core.*;
import com.karuslabs.scribe.core.parsers.CommandParser.Label;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Command(name = "name", aliases = {"a", "b"}, description = "desc", syntax = "syntax", permission = "a.b", message = "message")
class CommandResolverTest {
    
    @ParameterizedTest
    @MethodSource("values_parameters")
    void values(Label label, String name) {
        assertEquals(name, label.value);
    }
    
    static Stream<Arguments> values_parameters() {
        return Stream.of(
            of(Label.NAME, "name"),
            of(Label.ALIAS, "alias")
        );
    }
    
    
    CommandParser<Class<?>> resolver = new CommandParser<>();
    Environment<Class<?>> resolution = spy(new Environment<>());
    Command command = CommandResolverTest.class.getAnnotation(Command.class);
    
    
    @BeforeEach
    void before() {
        resolver.initialize(Project.EMPTY, Resolver.CLASS, resolution);
    }
    
    
    @Test
    void resolve() {
        resolver.parse(Set.of(CommandResolverTest.class));
        var mapping = resolution.mappings;
        
        var commands = (Map<String, Object>) mapping.get("commands");
        var command = (Map<String, Object>) commands.get("name");
        
        assertTrue(resolution.messages.isEmpty());
        
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
        resolver.check(Object.class, command, "invalid name", Label.NAME);
        
        assertTrue(resolver.names.isEmpty());
        assertEquals(
            Message.error(Object.class, "Invalid command name: 'invalid name', name cannot contain whitespaces"), 
            resolution.messages.get(0)
        );
    }
    
    
    @Test
    void check_empty_name() {
        resolver.check(Object.class, command, "", Label.NAME);
        
        assertTrue(resolver.names.isEmpty());
        assertEquals(
            Message.error(Object.class, "Invalid command name: '', name cannot be empty"), 
            resolution.messages.get(0)
        );
    }
    
    
    @Test
    void check_adds_name() {
        resolver.check(Object.class, command, "alias", Label.ALIAS);
        var entry = resolver.names.get("alias");
        
        assertTrue(resolution.messages.isEmpty());
        assertEquals(command, entry.getKey());
        assertEquals(Label.ALIAS, entry.getValue());
    }
    
    
    @ParameterizedTest
    @MethodSource("check_duplicate_namespace_parameters")
    void check_duplicate_namespace(Label current, Label previous, String expected) {
        resolver.names.put("something", new SimpleEntry<>(command, previous));
        
        resolver.check(Object.class, command, "something", current);
        
        assertEquals(
            Message.error(Object.class, expected), 
            resolution.messages.get(0)
        );
    }
    
    static Stream<Arguments> check_duplicate_namespace_parameters() {
        return Stream.of(
            of(Label.NAME, Label.NAME, "Conflicting command names: 'something', command names must be unique"),
            of(Label.ALIAS, Label.ALIAS, "Conflicting command aliases: 'something' for 'name' and 'name', command aliases must be unique"),
            of(Label.NAME, Label.ALIAS, "Conflicting command name and alias: 'something' and alias for 'name', command names and aliases must be unique")
        );
    }
    
    
    @Test
    void resolve_map() {
        var results = resolver.parse(Object.class, command);
        
        assertTrue(resolver.names.isEmpty());
        
        assertEquals(5, results.size());
        assertArrayEquals(new String[] {"a", "b"}, (String[]) results.get("aliases"));
        assertEquals("desc", results.get("description"));
        assertEquals("syntax", results.get("usage"));
        assertEquals("a.b", results.get("permission"));
        assertEquals("message", results.get("permission-message"));
    }
    
    
    @Test
    void resolve_warning() {
        var results = resolver.parse(Object.class, MalformedPermission.class.getAnnotation(Command.class));
        
        assertEquals(2, results.size()); 
        assertEquals(
            Message.warning(Object.class, "Potentially malformed command permission: 'a?s'"), 
            resolution.messages.get(0)
        );
    }
    
    
    @Command(name = "otherwise", permission = "a?s")
    static class MalformedPermission {
    
    }
    
    
    @Test
    void clear() {
        resolver.names.put("name", new AbstractMap.SimpleEntry<>(null, null));
        
        resolver.clear();
        
        assertTrue(resolver.names.isEmpty());
    }

} 
