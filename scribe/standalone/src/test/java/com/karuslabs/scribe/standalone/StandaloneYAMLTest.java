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
package com.karuslabs.scribe.standalone;

import java.io.*;
import javax.annotation.processing.*;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class StandaloneYAMLTest {
    
    Filer filer = mock(Filer.class);
    Messager messager = mock(Messager.class);
    StandaloneYAML yaml = new StandaloneYAML(filer, messager);
    
    
    @Test
    void writer() throws IOException {
        var writer = mock(Writer.class);
        FileObject object = when(mock(FileObject.class).openWriter()).thenReturn(writer).getMock();
        when(filer.createResource(any(), any(), any())).thenReturn(object);
        
        assertEquals(writer, yaml.writer());
        verify(filer).createResource(StandardLocation.CLASS_OUTPUT, "", "plugin.yml");
        verify(object).openWriter();
    }
    
    
    @Test
    void handle() {
        yaml.handle(new IOException("Message"));
        
        verify(messager).printMessage(Diagnostic.Kind.ERROR, "Failed to create plugin.yml");
        verify(messager).printMessage(Diagnostic.Kind.ERROR, "Message");
    }

} 
