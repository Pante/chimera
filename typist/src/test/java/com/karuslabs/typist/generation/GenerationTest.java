/*
 * The MIT License
 *
 * Copyright 2021 Karus Labs.
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
package com.karuslabs.typist.generation;

import com.karuslabs.elementary.junit.*;
import com.karuslabs.typist.Environment;
import com.karuslabs.typist.generation.chunks.*;
import com.karuslabs.typist.lexers.*;
import com.karuslabs.typist.lexers.Lexer.Memoizer;
import com.karuslabs.typist.parsers.CommandParser;
import com.karuslabs.utilitary.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Map;
import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(ToolsExtension.class)
class GenerationTest {
    
    static final LocalDateTime TIME = LocalDateTime.now();
    
    Logger logger = mock(Logger.class);
    Filer filer;
    StringWriter writer = new StringWriter();
    CommandParser commands = new CommandParser(logger, new CommandLexer(new ArgumentLexer(), LiteralLexer.aliasable(new Memoizer())));
    int[] counter = new int[] {0}; 
    Generation generation;
    Environment environment = new Environment();
    Element element = mock(Element.class);
    Source source = new Source();
    Cases cases = Tools.cases();
    
    @BeforeEach
    void before() throws IOException {
        JavaFileObject file = when(mock(JavaFileObject.class).openWriter()).thenReturn(writer).getMock();
        filer = when(mock(Filer.class).createSourceFile(any(), any(Element[].class))).thenReturn(file).getMock();
        generation = new Generation(logger, filer, new Header(), new Type(new MethodBody(new CommandInstantiation(Map.of(), counter))), counter);
    }
        
    @Test
    void generate_with_package() throws IOException {
        environment.out = new Out(element, "com.karuslabs", "Commands");
        generation.generate(environment);
        
        verify(filer).createSourceFile(eq("com.karuslabs.Commands"));
        assertTrue(writer.toString().startsWith("package com.karuslabs;"));
    }
    
    @Test
    void generate_without_package() throws IOException {
        environment.out = new Out(element, "", "Commands");
        generation.generate(environment);
        
        verify(filer).createSourceFile(eq("Commands"));
        assertFalse(writer.toString().startsWith("package com.karuslabs;"));
    }
    
    @Test
    void generate_FilerException() throws IOException {
        when(filer.createSourceFile(any(), any(Element[].class))).thenThrow(FilerException.class);
        
        environment.out = new Out(element, "com.karuslabs", "Commands");
        generation.generate(environment);
        
        verify(logger).error(element, "\"com.karuslabs.Commands\" already exists");
    }
    
    @Test
    void generate_IOException() throws IOException {
        when(filer.createSourceFile(any(), any(Element[].class))).thenThrow(IOException.class);
        
        environment.out = new Out(element, "com.karuslabs", "Commands");
        generation.generate(environment);
        
        verify(logger).error(element, "Failed to create file: \"com.karuslabs.Commands\"");
    }
    

} 
