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
package com.karuslabs.scribe.core;

import com.karuslabs.scribe.annotations.*;
import com.karuslabs.scribe.core.resolvers.PluginResolver;

import java.io.*;
import java.lang.annotation.Annotation;
import java.net.*;
import java.util.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ProcessorTest {
    
    
    StubProcessor processor = new StubProcessor();
    
    
    @Test
    void loader() throws MalformedURLException {
        var loader = Processor.loader(List.of(""));
        
        assertTrue(loader instanceof URLClassLoader);
    }
    
    
    @Test
    void run() {
        assertEquals(6, processor.resolvers.size());
        
        var resolver = processor.resolver;
        processor.resolvers = List.of(resolver);
        
        var resolution = processor.run();
        
        verify(resolver).initialize(processor.project, Extractor.CLASS, resolution);
        verify(resolver).resolve(Set.of(StubProcessor.class));
    }
    
    
    @Test
    void annotations() {
        var annotations = processor.annotations();
        
        assertNotNull(processor.annotations);
        assertEquals(
            Set.of(
                API.class,
                Command.class,
                Commands.class,
                Information.class,
                Load.class,
                Permission.class,
                Permissions.class,
                Plugin.class
            ), 
            annotations
        );
    }

}


class StubProcessor extends Processor<Class<?>> {
    
    PluginResolver<Class<?>> resolver;
    
    
    StubProcessor() {
        this(when(mock(PluginResolver.class).annotations()).thenReturn(Set.of(Plugin.class)).getMock());
    }
    
    StubProcessor(PluginResolver<Class<?>> resolver) {
        super(mock(Project.class), Extractor.CLASS, resolver);
        this.resolver = resolver;
    }

    @Override
    protected Stream<Class<?>> annotated(Class<? extends Annotation> annotation) {
        return Stream.of(StubProcessor.class);
    }

}
