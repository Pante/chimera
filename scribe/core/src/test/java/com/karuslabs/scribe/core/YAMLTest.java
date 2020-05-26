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

import java.io.*;
import java.util.Map;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class YAMLTest {
    
    StubYAML yaml = spy(new StubYAML());
    
    
    @Test
    void write() throws IOException {
        yaml.write(Map.of("k", "v"));
        
        verify(yaml.writer, times(2)).append(any(CharSequence.class));
        verify(yaml.writer).append("k: v\n");
    }
    
    
    @Test
    void handle() throws IOException {
        doThrow(IOException.class).when(yaml).writer();
        
        yaml.write(Map.of());
        
        verify(yaml).handle(any(IOException.class));
    }

}

class StubYAML extends YAML {

    Writer writer;
    
    
    StubYAML() {
        super("name");
        try {
            writer = mock(Writer.class);
            when(writer.append(any())).thenReturn(writer);
            
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    
    @Override
    protected Writer writer() throws IOException {
        return writer;
    }

    @Override
    protected void handle(IOException e) {
        
    }
    
}


class FileYAMLTest {
    
    File file = new File(new File(getClass().getClassLoader().getResource("beacon.yml").getFile()).getParentFile(), "plugin.yml");
    IOException exception = mock(IOException.class);
    YAML yaml = YAML.fromFile("name", file);
    
    
    @Test
    void writer() throws IOException {
        try (var writer = yaml.writer()) {
            assertNotNull(writer);
        }
    }
    
    
    @Test
    void handle() {
        assertTrue(assertThrows(UncheckedIOException.class, () -> yaml.handle(exception)).getCause() instanceof IOException);
    }
    
    
    @AfterEach
    void after() {
        if (file.exists()) {
            file.delete();
        }
    }

} 
