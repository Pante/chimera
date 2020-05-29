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

import com.karuslabs.scribe.annotations.Permission;
import com.karuslabs.scribe.core.*;

import java.util.*;

import org.junit.jupiter.api.*;

import static com.karuslabs.scribe.annotations.Default.FALSE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@Permission(value = "a.b", description = "desc", implicit = FALSE, children = {"a.b.c"})
class PermissionParserTest {
    
    Environment<Class<?>> environment = StubEnvironment.of(Project.EMPTY);
    PermissionParser<Class<?>> parser = new PermissionParser<>(environment);
    
    
    @Test
    void parse() {
        parser.parse(PermissionParserTest.class);
        var mappings = environment.mappings;
        
        var permissions = (Map<String, Object>) mappings.get("permissions");
        var permission = (Map<String, Object>) permissions.get("a.b");
        var children = (Map<String, Boolean>) permission.get("children");
        
        assertEquals(1, permissions.size());
        
        assertEquals(3, permission.size());
        assertEquals("desc", permission.get("description"));
        assertEquals("false", permission.get("default"));
        
        assertEquals(1, children.size());
        assertTrue(children.get("a.b.c"));
    }
    
    
    @Test
    void check_warn() {
        parser.names.add("a.b");
        parser.check(Warnings.class, Warnings.class.getAnnotation(Permission.class));
        
        verify(environment).warn(Warnings.class, "\"a.b\" inherits itself");
        verify(environment).error(Warnings.class, "\"a.b\" already exists");
    }
    
    
    @Permission(value = "a.b", children = {"a.b"})
    static class Warnings {
        
    }
    
    
    @Test
    void checkMalformed_warn() {
        parser.checkMalformed(Malformed.class, Malformed.class.getAnnotation(Permission.class));
        
        verify(environment, times(2)).warn(Malformed.class, "\"a b\" may be malformed");
        verify(environment).warn(Malformed.class, "\"ce!\" may be malformed");
    }
    
    
    @Permission(value = "a b", children = {"a b", "ce!", "e.f"})
    static class Malformed {
    
    }
    
    
    @Test
    void parse_permission() {
        var permissions = new HashMap<String, Object>();
        parser.parse(PermissionParserTest.class.getAnnotation(Permission.class), permissions);
        
        var permission = (Map<String, Object>) permissions.get("a.b");
        var children = (Map<String, Boolean>) permission.get("children");
        
        assertEquals(1, permissions.size());
        
        assertEquals(3, permission.size());
        assertEquals("desc", permission.get("description"));
        assertEquals("false", permission.get("default"));
        
        assertEquals(1, children.size());
        assertTrue(children.get("a.b.c"));
    }
    
    
    @Test
    void parse_permission_empty() {
        var permissions = new HashMap<String, Object>();
        parser.parse(Empty.class.getAnnotation(Permission.class), permissions);
        
        var permission = (Map<String, Object>) permissions.get("e.f");
        
        assertEquals(1, permissions.size());
        assertEquals(1, permission.size());
        assertEquals("op", permission.get("default"));
    }
    
    @Permission(value = "e.f")
    static class Empty {
        
    }
    
    
    @Test
    void clear() {
        parser.names.add("name");
        parser.clear();
        
        assertTrue(parser.names.isEmpty());
    }

} 
