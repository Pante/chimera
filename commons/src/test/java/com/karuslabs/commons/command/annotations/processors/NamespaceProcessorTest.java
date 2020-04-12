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
package com.karuslabs.commons.command.annotations.processors;

import com.karuslabs.commons.command.annotations.old.assembler.processors.NamespaceProcessor;
import com.karuslabs.commons.command.annotations.*;
import com.mojang.brigadier.arguments.ArgumentType;

import java.util.*;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class NamespaceProcessorTest {
    
    NamespaceProcessor processor = spy(new NamespaceProcessor());
    TypeMirror type = mock(TypeMirror.class);
    Element enclosing = when(mock(Element.class).asType()).thenReturn(type).getMock();
    Element element = when(mock(Element.class).accept(any(), any())).thenReturn(enclosing).getMock();
    
    
    @Test
    void process() {
        RoundEnvironment environment = mock(RoundEnvironment.class);
        
        when(element.getAnnotationsByType(Literal.class)).thenReturn(new Literal[] {});
        when(element.getAnnotationsByType(Argument.class)).thenReturn(new Argument[] {});
        
        doReturn(Set.of(element)).when(environment).getElementsAnnotatedWithAny(any(TypeElement[].class));
        
        assertFalse(processor.process(Set.of(), environment));
        assertTrue(processor.scopes.isEmpty());
    }
    
    
    @Test
    void scope() {
        var scope = processor.scope(element);
        assertSame(scope, processor.scopes.get(type.toString()));
    }
    
    
    @Test
    void process_scope() {
        when(element.getAnnotationsByType(Literal.class)).thenReturn(Namespace.class.getDeclaredAnnotationsByType(Literal.class));
        when(element.getAnnotationsByType(Argument.class)).thenReturn(Namespace.class.getDeclaredAnnotationsByType(Argument.class));
        
        doReturn(new HashSet<>()).when(processor).scope(element);
        
        processor.process(element);
    }
    
    @Literal(namespace = "a")
    @Argument(namespace = "b")
    static class Namespace {
        
        static @Bind ArgumentType<?> b;
        
    }
    
    
    
    @Test
    void process_namespace_aliases_duplicate() {
        doNothing().when(processor).warn(any(), any());
        
        processor.process(element, "Literal", new String[] {"a"}, new String[] {"a"});
        
        verify(processor).warn(element, "Duplicate alias: a");
        assertTrue(processor.existing.isEmpty());
    }
    
    
    @Test
    void process_scope_namespace_empty() {
        doNothing().when(processor).error(any(), any());
        
        processor.process(element, Set.of(), "Literal", new String[] {});
        
        verify(processor).error(element, "Invalid namespace for @Literal, namespace cannot be empty");
    }
    
    
    @Test
    void processor_scope_namespace() {
        doNothing().when(processor).error(any(), any());
        
        var set = new HashSet<List<String>>();
        set.add(List.of("a"));
        
        processor.process(element, set, "Literal", new String[] {"a"});
        
        verify(processor).error(element, "Invalid namespace for @Literal[a], namespace already exists");
    }

} 
