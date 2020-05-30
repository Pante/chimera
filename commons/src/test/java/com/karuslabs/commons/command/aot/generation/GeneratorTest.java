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
package com.karuslabs.commons.command.aot.generation;

import com.karuslabs.commons.command.aot.*;
import com.karuslabs.commons.command.aot.generation.blocks.*;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Set;
import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.tools.JavaFileObject;

import org.junit.jupiter.api.*;

import static com.karuslabs.commons.command.aot.Binding.*;
import static javax.lang.model.element.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class GeneratorTest {
    
    Environment environment = spy(new Environment(mock(Messager.class), mock(Filer.class), null, null));
    SourceResolver resolver = when(mock(SourceResolver.class).element()).thenReturn(mock(Element.class)).getMock();
    Generator generator = new Generator(environment, resolver, new TypeBlock(), new MethodBlock());
    
    StringWriter writer = new StringWriter();
    JavaFileObject file;
    
    TypeElement type = when(mock(TypeElement.class).getQualifiedName()).thenReturn(new StubName("aot.generation.TellCommand")).getMock();
    ExecutableElement command = when(mock(ExecutableElement.class).getSimpleName()).thenReturn(new StubName("b")).getMock();
    VariableElement argumentType = when(mock(VariableElement.class).getSimpleName()).thenReturn(new StubName("a")).getMock();
    VariableElement suggestions = when(mock(VariableElement.class).getSimpleName()).thenReturn(new StubName("suggestions")).getMock();
    
    
    @BeforeEach
    void before() throws IOException {
        when(resolver.folder()).thenReturn("aot.generation");
        when(resolver.file()).thenReturn("Commands");
        
        file = when(mock(JavaFileObject.class).openWriter()).thenReturn(writer).getMock();
        
        when(environment.filer.createSourceFile(any(), any())).thenReturn(file);
        
        when(command.getModifiers()).thenReturn(Set.of(PUBLIC, STATIC, FINAL));
        when(argumentType.getModifiers()).thenReturn(Set.of(PUBLIC, FINAL));
        when(suggestions.getModifiers()).thenReturn(Set.of(PUBLIC, STATIC, FINAL));
        
        var root = Token.root();
        
        var tell = Token.literal(mock(Element.class), "tell|t", "tell", Set.of("t"));
        tell.bindings.put(COMMAND, command);
        
        var argument = Token.argument(mock(Element.class), "<players>", "players");
        argument.bindings.put(TYPE, argumentType);
        argument.bindings.put(SUGGESTIONS, suggestions);
        
        root.add(environment, tell).add(environment, argument);
        
        environment.scopes.put(type, root);
    }
    
    
    @Test
    void generate() throws IOException, URISyntaxException {
        generator.generate();
        
        verify(environment, times(0)).error(any(), any());
        
        // Don't you just love issues with Windows replacing LF with CRLF?
        var expected = Files.readString(Paths.get(getClass().getClassLoader().getResource("aot/generation/Commands.java").toURI()), StandardCharsets.UTF_8);
        expected = expected.replaceAll("\\r", "");
        
        var actual = writer.toString().replaceFirst("(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{0,9})", "test-time").replaceAll("\\r", "");
        assertEquals(expected, actual);
    }
    
    
    @Test
    void generate_exists() throws IOException {
        when(file.openWriter()).thenThrow(FilerException.class);
        
        generator.generate();
        
        verify(environment).error(resolver.element(), "\"aot.generation.Commands\" already exists");
    }
    
    
    @Test
    void generate_error() throws IOException {
        when(file.openWriter()).thenThrow(IOException.class);
        
        generator.generate();
        
        verify(environment).error(resolver.element(), "Failed to create file: \"aot.generation.Commands\"");
    }

}
