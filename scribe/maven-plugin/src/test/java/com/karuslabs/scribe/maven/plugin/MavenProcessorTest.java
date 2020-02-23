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
package com.karuslabs.scribe.maven.plugin;

import com.karuslabs.scribe.annotations.Command;
import com.karuslabs.scribe.core.Project;

import io.github.classgraph.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MavenProcessorTest {
    
    ScanResult results = mock(ScanResult.class);
    ClassGraph graph = when(mock(ClassGraph.class).scan()).thenReturn(results).getMock();
    MavenProcessor processor = new MavenProcessor(Project.EMPTY, graph);
    
    
    @Test
    void annotated() {
        var info = mock(ClassInfo.class);
        doReturn(Command.class).when(info).loadClass();
        
        ClassInfoList list = when(mock(ClassInfoList.class).stream()).thenReturn(Stream.of(info)).getMock();
        when(results.getClassesWithAnnotation(Command.class.getName())).thenReturn(list);
        
        var actual = processor.annotated(Command.class).collect(toList());
        
        verify(graph).scan();
        verify(results).getClassesWithAnnotation(Command.class.getName());
        
        assertEquals(List.of(Command.class), actual);
    }
    
    
    @Test
    void close() {
        processor.results = results;
        
        processor.close();
        
        verify(results).close();
    }

} 
