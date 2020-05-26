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
import com.karuslabs.scribe.core.parsers.*;

import java.lang.annotation.Annotation;
import java.net.*;
import java.util.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ProcessorTest {
    
    StubProcessor processor = new StubProcessor();
    
    
    @Test
    void loader() throws MalformedURLException {
        var loader = Processor.loader(List.of(""));
        
        assertTrue(loader instanceof URLClassLoader);
    }
    
    
    @Test
    void run() {
        assertEquals(6, processor.parsers.size());
        
        Parser<Class<?>> parser = when(mock(Parser.class).annotations()).thenReturn(Set.of(Test.class)).getMock();
        processor.parsers = List.of(parser);
        
        processor.run();
        
        verify(parser).parse(Set.of(StubProcessor.class));
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
    
    PluginParser<Class<?>> resolver;
    
    
    StubProcessor() {
        this(when(mock(PluginParser.class).annotations()).thenReturn(Set.of(Plugin.class)).getMock());
    }
    
    StubProcessor(PluginParser<Class<?>> resolver) {
        super(mock(Environment.class), resolver);
        this.resolver = resolver;
    }

    @Override
    protected Stream<Class<?>> annotated(Class<? extends Annotation> annotation) {
        return Stream.of(StubProcessor.class);
    }

}
