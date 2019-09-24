/*
 * The MIT License
 *
 * Copyright 2019 Karus Labs.
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
package com.karuslabs.scribe.annotations.resolvers;

import com.karuslabs.scribe.annotations.Command;
import com.karuslabs.scribe.annotations.resolvers.CommandResolver.Type;

import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.stream.Stream;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.junit.jupiter.MockitoExtension;

import static com.karuslabs.scribe.annotations.processor.Resolver.COMMAND;

import static javax.tools.Diagnostic.Kind.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Command(name = "name", aliases = {"a", "b"}, description = "desc", syntax = "syntax", permission = "a.b", message = "message")
class CommandResolverTest {
    
    @ParameterizedTest
    @MethodSource("values_parameters")
    void values(Type type, String name) {
        assertEquals(name, type.name);
    }
    
    static Stream<Arguments> values_parameters() {
        return Stream.of(
            of(Type.NAME, "name"),
            of(Type.ALIAS, "alias")
        );
    }
    
    
    Element element = mock(Element.class);
    Messager messager = mock(Messager.class);
    CommandResolver resolver = new CommandResolver(messager);
    Command command =  CommandResolverTest.class.getAnnotation(Command.class);
    
    
    @Test
    void resolve() {
        when(element.getAnnotationsByType(Command.class)).thenReturn(new Command[] {command});
        var results = new HashMap<String, Object>();
        
        resolver.resolve(Set.of(element), results);
        
        var commands = (Map<String, Object>) results.get("commands");
        var command = (Map<String, Object>) commands.get("name");
        
        verifyZeroInteractions(messager);
        
        assertEquals(1, results.size());
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
        resolver.check(element, COMMAND.matcher(""), command, "invalid name", Type.NAME);
        
        verify(messager).printMessage(ERROR, "Invalid command name: invalid name, name cannot contain whitespaces", element);
        assertTrue(resolver.names.isEmpty());
    }
    
    
    @Test
    void check_empty_name() {
        resolver.check(element, COMMAND.matcher(""), command, "", Type.NAME);
        
        verify(messager).printMessage(ERROR, "Invalid command name: , name cannot be empty", element);
        assertTrue(resolver.names.isEmpty());
    }
    
    
    @Test
    void check_adds_name() {
        resolver.check(element, COMMAND.matcher(""), command, "alias", Type.ALIAS);
        var entry = resolver.names.get("alias");
        
        verifyZeroInteractions(messager);
        assertEquals(command, entry.getKey());
        assertEquals(Type.ALIAS, entry.getValue());
    }
    
    
    @ParameterizedTest
    @MethodSource("check_duplicate_namespace_parameters")
    void check_duplicate_namespace(Type current, Type previous, String expected) {
        resolver.names.put("something", new SimpleEntry<>(command, previous));
        
        resolver.check(element, COMMAND.matcher(""), command, "something", current);
        
        verify(messager).printMessage(ERROR, expected, element);
    }
    
    static Stream<Arguments> check_duplicate_namespace_parameters() {
        return Stream.of(
            of(Type.NAME, Type.NAME, "Conflicting command names: something, command names must be unique"),
            of(Type.ALIAS, Type.ALIAS, "Conflicting command aliases: something for name and name, command aliases must be unique"),
            of(Type.NAME, Type.ALIAS, "Conflicting command name and alias: something and alias for name, command names and aliases must be unique")
        );
    }
    
    
    @Test
    void resolve_map() {
        var results = resolver.resolve(element, command);
        
        verifyZeroInteractions(messager);
        
        assertEquals(5, results.size());
        assertArrayEquals(new String[] {"a", "b"}, (String[]) results.get("aliases"));
        assertEquals("desc", results.get("description"));
        assertEquals("syntax", results.get("usage"));
        assertEquals("a.b", results.get("permission"));
        assertEquals("message", results.get("permission-message"));
    }
    
    
    @Test
    void resolve_warning() {
        var results = resolver.resolve(element, MalformedPermission.class.getAnnotation(Command.class));
        
        verify(messager).printMessage(MANDATORY_WARNING, "Potentially malformed command permission: a?s", element);
        assertEquals(2, results.size());  
    }
    
    @Command(name = "otherwise", permission = "a?s")
    static class MalformedPermission {
    
    }
    
    
    @Test
    void clear() {
        resolver.names.put("name", new SimpleEntry<>(null, null));
        
        resolver.clear();
        
        assertTrue(resolver.names.isEmpty());
    }

} 
