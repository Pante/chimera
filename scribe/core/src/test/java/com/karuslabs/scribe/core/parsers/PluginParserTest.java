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

import com.karuslabs.scribe.annotations.Plugin;
import com.karuslabs.scribe.core.*;

import java.util.*;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.*;

import org.bukkit.plugin.java.JavaPlugin;

import org.junit.jupiter.api.*;

import static com.karuslabs.annotations.processor.Messages.quote;
import static javax.lang.model.element.Modifier.ABSTRACT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@Plugin(name = "name1", version ="1.0.0")
class PluginParserTest {
    
    Environment<Class<?>> environment = StubEnvironment.of(new Project("project_name", "1.0.0", "", List.of(), "", ""));
    StubParser parser = new StubParser(environment);
    
    
    @Test
    void check() {
        parser.check(Set.of(PluginParserTest.class));
        
        verify(environment, times(0)).error(any(), any());
        verify(environment, times(0)).warn(any(), any());
    }
    
    
    @Test
    void check_none() {
        parser.check(Set.of()); 
        
        verify(environment).error("Project does not contain a @Plugin annotation, should contain one @Plugin annotation");
    }
    
    
    @Test
    void check_many() {
        parser.check(Set.of(Errors.class, Empty.class));
        
        verify(environment).error(Errors.class, "Project contains 2 @Plugin annotations, should contain one @Plugin annotation");
        verify(environment).error(Empty.class, "Project contains 2 @Plugin annotations, should contain one @Plugin annotation");
    }
    
    
    @Test
    void parse() {
        parser.parse(PluginParserTest.class);
        
        verify(environment, times(0)).error(any(), any());
        verify(environment, times(0)).warn(any(), any());
        
        assertEquals(PluginParserTest.class.getName(), environment.mappings.get("main"));
        assertEquals("name1", environment.mappings.get("name"));
        assertEquals("1.0.0", environment.mappings.get("version"));
    }
    
    
    @Test
    void parse_errors() {
        parser.parse(Errors.class);
        
        
        verify(environment).error(Errors.class, "\"a !\" is not a valid plugin name, should contain only alphanumeric characters and \"_\"");
        verify(environment).warn(Errors.class, "\"1\" may be malformed, version should follow SemVer, https://semver.org/");
    }
    
    
    @Plugin(name = "a !", version = "1")
    static class Errors {
        
    }
    
    
    @Test
    void parse_empty() {
        parser.parse(Empty.class);
        
        verify(environment, times(0)).error(any(), any());
        verify(environment, times(0)).warn(any(), any());
        
        assertEquals("project_name", environment.mappings.get("name"));
        assertEquals("1.0.0", environment.mappings.get("version"));
    }
    
    
    @Plugin
    static class Empty {
        
    }
    
    
    static class StubParser extends PluginParser<Class<?>> {

        StubParser(Environment<Class<?>> environment) {
            super(environment);
        }

        @Override
        protected void check(Class<?> type) {
            
        }
        
        @Override
        protected String stringify(Class<?> type) {
            return type.getName();
        }

    }
    
}


class ClassPluginParserTest {
    
    Environment<Class<?>> environment = StubEnvironment.of(Project.EMPTY);
    PluginParser<Class<?>> parser = PluginParser.type(environment);
    
    
    @Test
    void check() {
        parser.check(Valid.class);
        
        verify(environment, times(0)).error(any(), any());
        verify(environment, times(0)).warn(any(), any());
    }
    
    
    static class Valid extends JavaPlugin {
        
    }
    
    
    @Test
    void check_abstract() {
        parser.check(Abstract.class);
        
        verify(environment).error(Abstract.class, quote(Abstract.class.getName()) + " is not a valid main class, should not be abstract");
    }
    
    static abstract class Abstract extends JavaPlugin {
        
    }
    
    
    @Test
    void check_superclass() {
        parser.check(ClassPluginParserTest.class);
        
        verify(environment).error(
            ClassPluginParserTest.class, 
            quote(ClassPluginParserTest.class.getName()) + " is not a valid main class, should inherit from " + quote(org.bukkit.plugin.Plugin.class.getName())
        );
    }
    
    
    @Test
    void check_constructor() {
        parser.check(Constructor.class);

        verify(environment).error(Constructor.class, quote(Constructor.class.getName()) + " is not a valid main class, should not contain a constructor with parameters");
    }
    
    
    @Test
    void stringify() {
        assertEquals(Constructor.class.getName(), parser.stringify(Constructor.class));
    }
    
    
    static class Constructor extends JavaPlugin {
        
        Constructor() {}
        
        Constructor(int a) {}
        
    }
    
}


class ElementPluginParserTest {
    
    TypeMirror mirror = mock(TypeMirror.class);
    TypeElement type = when(mock(TypeElement.class).asType()).thenReturn(mirror).getMock();
    Elements elements = when(mock(Elements.class).getTypeElement(org.bukkit.plugin.Plugin.class.getName())).thenReturn(type).getMock();
    
    Environment<Element> environment = spy(new StubEnvironment<>(Project.EMPTY, Resolver.ELEMENT));
    ExecutableElement executable = mock(ExecutableElement.class);
    Element element = mock(Element.class);
    Types types = mock(Types.class);
    
    ElementPluginParser parser = (ElementPluginParser) PluginParser.element(environment, elements, types);;
    
    
    @Test
    void check() {
        when(element.accept(parser.visitor, false)).thenReturn(true);
        
        parser.check(element);
        
        verify(element).accept(parser.visitor, false);
    }
    
    
    @Test
    void stringify() {
        assertEquals(mirror.toString(), parser.stringify(type));
    }
    
    
    @Test
    void visitor_visitType() {
        when(types.isAssignable(type.asType(), mirror)).thenReturn(true);
        when(type.getEnclosedElements()).thenReturn(List.of());
        
        assertTrue(parser.visitor.visitType(type, false));
    }
    
    
    @Test
    void visitor_visitType_abstract() {
        when(type.getModifiers()).thenReturn(Set.of(ABSTRACT));
        
        parser.visitor.visitType(type, false);
        
        verify(environment).error(type, quote(type.asType()) + " is not a valid main class, should not be abstract");
    }
    
    
    @Test
    void visitor_visitType_nested() {
        assertTrue(parser.visitor.visitType(type, true));
    }
    
    
    @Test
    void visitor_visitType_invalid_type() {
        when(types.isAssignable(type.asType(), mirror)).thenReturn(false);
        when(type.getEnclosedElements()).thenReturn(List.of());
        
        assertFalse(parser.visitor.visitType(type, false));
        
        verify(environment).error(
            type, 
            quote(type.asType()) + " is not a valid main class, should inherit from " + quote(org.bukkit.plugin.Plugin.class.getName())
        );
    }
    
    
    @Test
    void visitor_visitType_enclosed() {
        Element enclosed = when(mock(Element.class).accept(parser.visitor, true)).thenReturn(false).getMock();
        
        when(types.isAssignable(type.asType(), mirror)).thenReturn(true);
        doReturn(List.of(enclosed)).when(type).getEnclosedElements();
        
        assertFalse(parser.visitor.visitType(type, false));
    }
    
    
    @Test
    void visitor_visitExecutable() {
        when(executable.getKind()).thenReturn(ElementKind.METHOD);
        doReturn(List.of(mock(VariableElement.class))).when(executable).getParameters();
        
        assertTrue(parser.visitor.visitExecutable(executable, false));
        
        verify(environment, times(0)).error(any(), any());
        verify(environment, times(0)).warn(any(), any());
    }
    
    
    @Test
    void visitor_visitExecutable_constructor() {
        when(executable.getKind()).thenReturn(ElementKind.CONSTRUCTOR);
        doReturn(List.of(mock(VariableElement.class))).when(executable).getParameters();
        
        assertFalse(parser.visitor.visitExecutable(executable, false));
        
        verify(environment).error(
            executable, 
            quote(executable.getEnclosingElement()) + " is not a valid main class, should not contain a constructor with parameters"
        );
    }
    
}