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

import com.karuslabs.scribe.annotations.Plugin;

import java.lang.annotation.Annotation;
import java.util.*;
import javax.annotation.processing.Messager;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.*;

import org.bukkit.plugin.java.JavaPlugin;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static javax.tools.Diagnostic.Kind.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PluginResolverTest {
    
    @Plugin(name = "test", version = "1.0.0")
    static class TestPlugin extends JavaPlugin {
        
    }
    
    
    Element element = mock(Element.class);
    Messager messager = mock(Messager.class);
    Elements elements = mock(Elements.class);
    Types types = mock(Types.class);
    TypeMirror mirror = mock(TypeMirror.class);
    TypeElement type = when(mock(TypeElement.class).asType()).thenReturn(mirror).getMock();
    ExecutableElement executable = mock(ExecutableElement.class);
    PluginResolver resolver;
    Plugin invalid = new Plugin() {
        @Override
        public String name() {
            return "Awesome Plugin";
        }

        @Override
        public String version() {
            return "1-alpha";
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return Plugin.class;
        }
        
    };
    
    
    @BeforeEach
    void before() {
        when(elements.getTypeElement(org.bukkit.plugin.Plugin.class.getName())).thenReturn(type);
        resolver = new PluginResolver(elements, types, messager);
    }
    
    
    @Test
    void check() {
        when(element.accept(resolver.visitor, false)).thenReturn(true);
        
        assertTrue(resolver.check(Set.of(element)));
        
        verifyZeroInteractions(messager);
        verify(element).accept(resolver.visitor, false);
    }
    
    
    @Test
    void check_empty() {        
        assertFalse(resolver.check(Set.of()));
        
        verify(messager).printMessage(ERROR, "No @Plugin annotation found, plugin must contain a @Plugin annotation");
    }
    
    
    @Test
    void check_multiple_plugins() {
        var a = mock(Element.class);
        var b = mock(Element.class);
        
        assertFalse(resolver.check(Set.of(a, b)));
        
        verify(messager, times(2)).printMessage(any(), any(), any());
        verify(messager).printMessage(ERROR, "Invalid number of @Plugin annotations, plugin must contain only one @Plugin annotation", a);
        verify(messager).printMessage(ERROR, "Invalid number of @Plugin annotations, plugin must contain only one @Plugin annotation", b);
    }
    
    
    @Test
    void resolve() {
        var results = new HashMap<String, Object>();
        
        when(element.getAnnotation(Plugin.class)).thenReturn(TestPlugin.class.getAnnotation(Plugin.class));
        when(mirror.toString()).thenReturn(TestPlugin.class.getName());
        when(element.asType()).thenReturn(mirror);
        
        resolver.resolve(element, results);
        
        verifyZeroInteractions(messager);
        
        assertEquals(3, results.size());
        assertEquals(TestPlugin.class.getName(), results.get("main"));
        assertEquals("test", results.get("name"));
        assertEquals("1.0.0", results.get("version"));
    }
    
    
    @Test
    void resolve_invalid() {
        var results = new HashMap<String, Object>();
        
        when(element.getAnnotation(Plugin.class)).thenReturn(invalid);
        when(element.asType()).thenReturn(mirror);
        
        resolver.resolve(element, results);
        
        verify(messager).printMessage(ERROR, "Invalid name: 'Awesome Plugin', name must contain only alphanumeric characters and '_'", element);
        verify(messager).printMessage(
            MANDATORY_WARNING, 
            "Unconventional versioning scheme: '1-alpha', " +
            "it is highly recommended that versions follows SemVer, https://semver.org/", 
            element
        );
    }
    
    
    @Test
    void visitor_visitType() {
        when(types.isAssignable(type.asType(), mirror)).thenReturn(true);
        when(type.getEnclosedElements()).thenReturn(List.of());
        
        assertTrue(resolver.visitor.visitType(type, false));
    }
    
    
    @Test
    void visitor_visitType_nested() {
        assertTrue(resolver.visitor.visitType(type, true));
    }
    
    
    @Test
    void visitor_visitType_invalid_type() {
        when(types.isAssignable(type.asType(), mirror)).thenReturn(false);
        when(type.getEnclosedElements()).thenReturn(List.of());
        
        assertFalse(resolver.visitor.visitType(type, false));
        
        verify(messager).printMessage(
            ERROR, 
            "Invalid main class: '" + type.asType() + "', main class must inherit from '" + org.bukkit.plugin.Plugin.class.getName() + "'", 
            type
        );
    }
    
    
    @Test
    void visitor_visitType_enclosed() {
        Element enclosed = when(mock(Element.class).accept(resolver.visitor, true)).thenReturn(false).getMock();
        
        when(types.isAssignable(type.asType(), mirror)).thenReturn(true);
        doReturn(List.of(enclosed)).when(type).getEnclosedElements();
        
        assertFalse(resolver.visitor.visitType(type, false));
    }
    
    
    @Test
    void visitor_visitExecutable() {
        when(executable.getKind()).thenReturn(ElementKind.METHOD);
        doReturn(List.of(mock(VariableElement.class))).when(executable).getParameters();
        
        assertTrue(resolver.visitor.visitExecutable(executable, false));
        
        verifyZeroInteractions(messager);
    }
    
    
    @Test
    void visitor_visitExecutable_constructor() {
        when(executable.getKind()).thenReturn(ElementKind.CONSTRUCTOR);
        doReturn(List.of(mock(VariableElement.class))).when(executable).getParameters();
        
        assertFalse(resolver.visitor.visitExecutable(executable, false));
        
        verify(messager).printMessage(
            ERROR, 
            "Invalid main class: '" + executable.getEnclosingElement() + "', main class cannot contain constructors with parameters",
            executable
        );
    }

} 
