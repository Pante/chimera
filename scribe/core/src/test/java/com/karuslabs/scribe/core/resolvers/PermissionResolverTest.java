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
package com.karuslabs.scribe.core.resolvers;

import com.karuslabs.scribe.annotations.Permission;
import com.karuslabs.scribe.core.*;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.karuslabs.scribe.annotations.Default.FALSE;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
@Permission(value = "a.b", description = "desc", implicit = FALSE, children = {"a.b.c"})
class PermissionResolverTest {
    
    PermissionResolver<Class<?>> resolver = new PermissionResolver<>();
    Resolution<Class<?>> resolution = new Resolution<>();
    
    
    @BeforeEach
    void before() {
        resolver.initialize(Project.EMPTY, Extractor.CLASS, resolution);
    }
    
    
    @Test
    void resolve() {
        resolver.resolve(PermissionResolverTest.class);
        var mappings = resolution.mappings;
        
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
    void check_warning() {
        resolver.names.add("a.b");
        resolver.check(Warnings.class, Warnings.class.getAnnotation(Permission.class));
        
        assertEquals(Message.warning(Warnings.class, "Self-inheriting permission: 'a.b'"), resolution.messages.get(0));
        assertEquals(Message.error(Warnings.class, "Conflicting permissions: 'a.b', permissions must be unique"), resolution.messages.get(1));
    }
    
    
    @Permission(value = "a.b", children = {"a.b"})
    static class Warnings {
        
    }
    
    
    @Test
    void checkMalformed_warning() {
        resolver.checkMalformed(Malformed.class, Malformed.class.getAnnotation(Permission.class));
        
        assertEquals(Message.warning(Malformed.class, "Potentially malformed permission: 'a b'"), resolution.messages.get(0));
        assertEquals(Message.warning(Malformed.class, "Potentially malformed child permission: 'a b'"), resolution.messages.get(1));
        assertEquals(Message.warning(Malformed.class, "Potentially malformed child permission: 'ce!'"), resolution.messages.get(2));
    }
    
    
    @Permission(value = "a b", children = {"a b", "ce!", "e.f"})
    static class Malformed {
    
    }
    
    
    @Test
    void resolve_permission() {
        var permissions = new HashMap<String, Object>();
        resolver.resolve(PermissionResolverTest.class.getAnnotation(Permission.class), permissions);
        
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
    void resolve_permission_empty() {
        var permissions = new HashMap<String, Object>();
        resolver.resolve(Empty.class.getAnnotation(Permission.class), permissions);
        
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
        resolver.names.add("name");
        resolver.clear();
        
        assertTrue(resolver.names.isEmpty());
    }

} 
