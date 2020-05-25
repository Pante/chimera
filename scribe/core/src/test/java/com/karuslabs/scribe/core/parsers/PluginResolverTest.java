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

import com.karuslabs.scribe.core.parsers.ElementPluginResolver;
import com.karuslabs.scribe.core.parsers.PluginParser;
import com.karuslabs.scribe.maven.plugin.Message;
import com.karuslabs.scribe.annotations.Plugin;
import com.karuslabs.scribe.core.*;

import java.util.*;
import javax.lang.model.element.*;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.*;

import org.bukkit.plugin.java.JavaPlugin;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static javax.lang.model.element.Modifier.ABSTRACT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Plugin(name = "name1", version ="1.0.0")
class PluginResolverTest {
    
    StubResolver resolver = new StubResolver();
    Environment<Class<?>> resolution = new Environment<>();
    Project project = new Project("project_name", "1.0.0", "", List.of(), "", "");
    
    
    @BeforeEach
    void before() {
        resolver.initialize(project, Resolver.CLASS, resolution);
    }
    
    
    @Test
    void check() {
        resolver.check(Set.of(PluginResolverTest.class));
        assertTrue(resolution.messages.isEmpty());
    }
    
    
    @Test
    void check_none() {
        resolver.check(Set.of()); 
        assertEquals(
            Message.error(null, "No @Plugin annotation found, plugin must contain a @Plugin annotation"),
            resolution.messages.get(0)
        );
    }
    
    
    @Test
    void check_many() {
        resolver.check(Set.of(Errors.class, Empty.class));
        assertEquals(
            Set.of(
                Message.error(Empty.class, "Invalid number of @Plugin annotations, plugin must contain only one @Plugin annotation"),
                Message.error(Errors.class, "Invalid number of @Plugin annotations, plugin must contain only one @Plugin annotation")
            ), 
            new HashSet<>(resolution.messages)
        );
    }
    
    
    @Test
    void resolve() {
        resolver.parse(PluginResolverTest.class);
        
        assertTrue(resolution.messages.isEmpty());
        assertEquals(PluginResolverTest.class.getName(), resolution.mappings.get("main"));
        assertEquals("name1", resolution.mappings.get("name"));
        assertEquals("1.0.0", resolution.mappings.get("version"));
    }
    
    
    @Test
    void resolve_errors() {
        resolver.parse(Errors.class);
        
        assertEquals(2, resolution.messages.size());
        assertEquals(
            Set.of(
                Message.error(Errors.class, "Invalid name: 'a !', name must contain only alphanumeric characters and '_'"),
                Message.warning(
                    Errors.class, 
                    "Unconventional versioning scheme: '1', it is highly recommended that versions follows SemVer, https://semver.org/"
                )
            ),
            new HashSet<>(resolution.messages)
        );
    }
    
    
    @Plugin(name = "a !", version = "1")
    static class Errors {
        
    }
    
    
    @Test
    void resolve_empty() {
        resolver.parse(Empty.class);
        
        assertTrue(resolution.messages.isEmpty());
        assertEquals("project_name", resolution.mappings.get("name"));
        assertEquals("1.0.0", resolution.mappings.get("version"));
    }
    
    
    @Plugin
    static class Empty {
        
    }
    
    
    static class StubResolver extends PluginParser<Class<?>> {

        @Override
        protected void check(Class<?> type) {
            
        }
        
        @Override
        protected String stringify(Class<?> type) {
            return type.getName();
        }

    }
    
}


@ExtendWith(MockitoExtension.class)
class ClassPluginResolverTest {
    
    PluginParser<Class<?>> resolver = PluginParser.CLASS;
    Environment<Class<?>> resolution = new Environment<>();
    
    
    @BeforeEach
    void before() {
        resolver.initialize(Project.EMPTY, Resolver.CLASS, resolution);
    }
    
    
    @Test
    void check() {
        resolver.check(Valid.class);
        assertTrue(resolution.messages.isEmpty());
        
    }
    
    
    static class Valid extends JavaPlugin {
        
    }
    
    
    @Test
    void check_abstract() {
        resolver.check(Abstract.class);
        assertEquals(
            Message.error(Abstract.class, "Invalid main class: '" + Abstract.class.getName() + "', main class cannot be abstract"),
            resolution.messages.get(0)
        );
    }
    
    static abstract class Abstract extends JavaPlugin {
        
    }
    
    
    @Test
    void check_superclass() {
        resolver.check(ClassPluginResolverTest.class);
        assertEquals(
            Message.error(
                ClassPluginResolverTest.class, 
                "Invalid main class: '" + ClassPluginResolverTest.class.getName() + "', main class must inherit from '" + org.bukkit.plugin.Plugin.class.getName() + "'"
            ),
            resolution.messages.get(0)
        );
    }
    
    
    @Test
    void check_constructor() {
        resolver.check(Constructor.class);
        assertEquals(1, resolution.messages.size());
        assertEquals(
            Message.error(
                Constructor.class, 
                "Invalid main class: '" + Constructor.class.getName() + "', main class cannot contain constructors with parameters"
            ),
            resolution.messages.get(0)
        );
    }
    
    
    @Test
    void stringify() {
        assertEquals(Constructor.class.getName(), resolver.stringify(Constructor.class));
    }
    
    
    static class Constructor extends JavaPlugin {
        
        Constructor() {}
        
        Constructor(int a) {}
        
    }
    
}


@ExtendWith(MockitoExtension.class)
class ElementPluginResolverTest {
    
    Element element = mock(Element.class);
    Elements elements = mock(Elements.class);
    Types types = mock(Types.class);
    TypeMirror mirror = mock(TypeMirror.class);
    TypeElement type = when(mock(TypeElement.class).asType()).thenReturn(mirror).getMock();
    ExecutableElement executable = mock(ExecutableElement.class);
    ElementPluginResolver resolver;
    Environment<Element> resolution = new Environment<>();
    
    
    @BeforeEach
    void before() {
        when(elements.getTypeElement(org.bukkit.plugin.Plugin.class.getName())).thenReturn(type);
        resolver = (ElementPluginResolver) PluginParser.element(elements, types);
        resolver.initialize(Project.EMPTY, Resolver.ELEMENT, resolution);
    }
    
    
    @Test
    void check() {
        when(element.accept(resolver.visitor, false)).thenReturn(true);
        
        resolver.check(element);
        
        verify(element).accept(resolver.visitor, false);
    }
    
    
    @Test
    void stringify() {
        assertEquals(mirror.toString(), resolver.stringify(type));
    }
    
    
    @Test
    void visitor_visitType() {
        when(types.isAssignable(type.asType(), mirror)).thenReturn(true);
        when(type.getEnclosedElements()).thenReturn(List.of());
        
        assertTrue(resolver.visitor.visitType(type, false));
    }
    
    
    @Test
    void visitor_visitType_abstract() {
        when(type.getModifiers()).thenReturn(Set.of(ABSTRACT));
        
        resolver.visitor.visitType(type, false);
        
        assertEquals(
            Message.error(type, "Invalid main class: '" + type.asType() +"', main class cannot be abstract"),
            resolution.messages.get(0)
        );
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
        
        assertEquals(
            Message.error(
                type, 
                "Invalid main class: '" + type.asType() + "', main class must inherit from '" + org.bukkit.plugin.Plugin.class.getName() + "'"
            ),
            resolution.messages.get(0)
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
        
        assertTrue(resolution.messages.isEmpty());
    }
    
    
    @Test
    void visitor_visitExecutable_constructor() {
        when(executable.getKind()).thenReturn(ElementKind.CONSTRUCTOR);
        doReturn(List.of(mock(VariableElement.class))).when(executable).getParameters();
        
        assertFalse(resolver.visitor.visitExecutable(executable, false));
        
        assertEquals(
            Message.error(
                executable, 
                "Invalid main class: '" + executable.getEnclosingElement() + "', main class cannot contain constructors with parameters"
            ),
            resolution.messages.get(0)
        );
    }
    
}