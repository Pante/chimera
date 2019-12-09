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
package com.karuslabs.scribe.standalone.resolvers;

import com.karuslabs.scribe.annotations.*;

import java.lang.annotation.Annotation;
import java.util.*;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.karuslabs.scribe.annotations.Default.*;
import static javax.tools.Diagnostic.Kind.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Permission(value = "a.b", description = "desc", implicit = FALSE, children = {"a.b.c"})
class PermissionResolverTest {
    
    Element element = mock(Element.class);
    Messager messager = mock(Messager.class);
    PermissionResolver resolver = new PermissionResolver(messager);
    
    
    @Test
    void resolve() {
        var results = new HashMap<String, Object>();
        when(element.getAnnotationsByType(Permission.class)).thenReturn(new Permission[] {PermissionResolverTest.class.getAnnotation(Permission.class)});
        
        resolver.resolve(element, results);
        
        var permissions = (Map<String, Object>) results.get("permissions");
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
        
        resolver.check(element, new Permission() {
            @Override
            public String value() {
                return "a.b";
            }

            @Override
            public String description() {
                return "";
            }

            @Override
            public Default implicit() {
                return Default.FALSE;
            }

            @Override
            public String[] children() {
                return new String[] {"a.b"};
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Permission.class;
            }
        });
        
        verify(messager, times(2)).printMessage(any(), any(), any());
        verify(messager).printMessage(MANDATORY_WARNING, "Self-inheriting permission: 'a.b'", element);
        verify(messager).printMessage(ERROR, "Conflicting permissions: 'a.b', permissions must be unique", element);
    }
    
    
    @Test
    void checkMalformed_warning() {
        resolver.checkMalformed(element, MalformedPermission.class.getAnnotation(Permission.class));
        
        verify(messager, times(3)).printMessage(any(), any(), any());
        verify(messager).printMessage(MANDATORY_WARNING, "Potentially malformed permission: 'a b'", element);
        verify(messager).printMessage(MANDATORY_WARNING, "Potentially malformed child permission: 'a b'", element);
        verify(messager).printMessage(MANDATORY_WARNING, "Potentially malformed child permission: 'ce!'", element);
    }
    
    
    @Permission(value = "a b", children = {"a b", "ce!", "e.f"})
    static class MalformedPermission {
    
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
        resolver.resolve(EmptyPermission.class.getAnnotation(Permission.class), permissions);
        
        var permission = (Map<String, Object>) permissions.get("e.f");
        
        assertEquals(1, permissions.size());
        assertEquals(1, permission.size());
        assertEquals("op", permission.get("default"));
    }
    
    @Permission(value = "e.f")
    static class EmptyPermission {
        
    }
    
    
    @Test
    void clear() {
        resolver.names.add("name");
        resolver.clear();
        
        assertTrue(resolver.names.isEmpty());
    }

} 
