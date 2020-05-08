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
package com.karuslabs.commons.command.aot;

import com.karuslabs.commons.command.aot.annotations.Emit;
import com.karuslabs.commons.command.aot.generation.*;
import com.karuslabs.commons.command.aot.parsers.*;

import java.util.Set;
import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ProcessorTest {
    
    Processor processor = spy(new Processor());
    Element a = mock(Element.class);
    Element b = mock(Element.class);
    RoundEnvironment round = mock(RoundEnvironment.class);
    
    
    @BeforeEach
    void before() {
        doNothing().when(processor).error(any());
        doNothing().when(processor).error(any(), any());
        
        processor.environment = mock(Environment.class);
        processor.command = mock(CommandParser.class);
        processor.bind = mock(BindParser.class);
        processor.analyzer = mock(Analyzer.class);
        processor.packager = mock(Packager.class);
        processor.generator = mock(Generator.class);
        processor.compiled = false;
    }
    
    
    @Test
    void init() {
        TypeMirror mirror = mock(TypeMirror.class);
        TypeElement type = when(mock(TypeElement.class).asType()).thenReturn(mirror).getMock();
        Elements elements = when(mock(Elements.class).getTypeElement(any(CharSequence.class))).thenReturn(type).getMock();

        var env = mock(ProcessingEnvironment.class);
        
        when(env.getMessager()).thenReturn(mock(Messager.class));
        when(env.getFiler()).thenReturn(mock(Filer.class));
        when(env.getElementUtils()).thenReturn(elements);
        when(env.getTypeUtils()).thenReturn(mock(Types.class));
        
        new Processor().init(env);
        
        assertNotNull(processor.environment);
        assertNotNull(processor.command);
        assertNotNull(processor.bind);
        assertNotNull(processor.analyzer);
        assertNotNull(processor.packager);
        assertNotNull(processor.generator);
        assertNotNull(processor.compiled);
    }
    
    
    @Test
    void process() {
        doReturn(Set.of()).when(round).getElementsAnnotatedWith(Emit.class);
        doReturn(false).when(processor.environment).error();
        doNothing().when(processor).resolveFile(Set.of());
        doNothing().when(processor).parse(any(Parser.class), eq(round), any());
        
        processor.process(Set.of(), round);
        
        assertTrue(processor.compiled);
        verify(processor).resolveFile(Set.of());
        verify(processor, times(2)).parse(any(Parser.class), eq(round), any());
        verify(processor.analyzer).analyze();
        verify(processor.generator).generate();
        verify(processor.environment).clear();
    }
    
    
    @Test
    void process_compiled() {
        processor.compiled = true;
        
        processor.process(Set.of(), round);
        
        verify(processor, times(0)).resolveFile(any());
        verify(processor, times(0)).parse(any(), any(), any());
        verifyNoInteractions(processor.analyzer);
        verifyNoInteractions(processor.generator);
    }
    
    
    @Test
    void process_error() {
        doReturn(Set.of()).when(round).getElementsAnnotatedWith(Emit.class);
        doReturn(true).when(processor.environment).error();
        doNothing().when(processor).resolveFile(Set.of());
        doNothing().when(processor).parse(any(Parser.class), eq(round), any());
        
        processor.process(Set.of(), round);
        
        assertTrue(processor.compiled);
        verify(processor).resolveFile(Set.of());
        verify(processor, times(2)).parse(any(Parser.class), eq(round), any());
        verify(processor.analyzer).analyze();
        verify(processor.environment).clear();
        
        verifyNoInteractions(processor.generator);
    }
    
    
    @Test
    void resolve_file() {
        processor.resolveFile(Set.of(a));
        
        verify(processor.packager).resolve(a);
    }
    
    
    @Test
    void resolve_file_none() {
        processor.resolveFile(Set.of());
        
        verify(processor).error("Project does not contain a @Pack annotation, should contain one @Pack annotation");
    }
    
    
    @Test
    void resolve_file_several() {
        processor.resolveFile(Set.of(a, b));
        
        verify(processor).error(a, "Project contains 2 @Pack annotations, should contain one @Pack annotation");
        verify(processor).error(b, "Project contains 2 @Pack annotations, should contain one @Pack annotation");
    }
    
    
    @Test
    void parse() {
        var parser = mock(Parser.class);
        doReturn(Set.of(a, b)).when(round).getElementsAnnotatedWith(Emit.class);
        
        processor.parse(parser, round, Emit.class);
        
        verify(parser).parse(a);
        verify(parser).parse(b);
    }

} 
