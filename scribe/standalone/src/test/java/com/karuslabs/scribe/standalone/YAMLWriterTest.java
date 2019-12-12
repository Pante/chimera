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
package com.karuslabs.scribe.standalone;

import java.io.*;
import java.util.Map;
import javax.annotation.processing.*;
import javax.tools.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static javax.tools.Diagnostic.Kind.ERROR;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class YAMLWriterTest {
    
    FileObject file = mock(FileObject.class);
    Filer filer = mock(Filer.class);
    Messager messager = mock(Messager.class);
    YAMLWriter writer = new YAMLWriter(filer, messager);
    
    
    @Test
    void write() throws IOException {
        var writer = mock(Writer.class);
        when(writer.append(any(CharSequence.class))).thenReturn(writer);
        when(file.openWriter()).thenReturn(writer);
        when(filer.createResource(StandardLocation.CLASS_OUTPUT, "", "plugin.yml")).thenReturn(file);
        
        this.writer.write(Map.of("k", "v"));
        
        verify(writer, times(2)).append(any(CharSequence.class));
        verify(writer).append("k: v\n");
    }
    
    
    @Test
    void write_throws_exception() throws IOException {
        when(filer.createResource(any(), any(), any(), any())).thenThrow(new IOException("message"));
        
        writer.write("Hi");
        
        verify(messager).printMessage(ERROR, "Failed to create plugin.yml");
        verify(messager).printMessage(ERROR, "message");
    }

} 
