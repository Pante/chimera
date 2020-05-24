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

import com.karuslabs.commons.command.aot.Environment;
import com.karuslabs.commons.command.aot.annotations.Source;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;
import javax.lang.model.element.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


class SourceResolverTest {
    
    PackageElement pack = when(mock(PackageElement.class).getQualifiedName()).thenReturn(new StubName("com.karuslabs.commands.aot")).getMock();
    Element element = when(mock(Element.class).accept(any(), any())).thenReturn(pack).getMock();
    Environment environment = mock(Environment.class);
    SourceResolver packager = new SourceResolver(environment);
    
    
    @ParameterizedTest
    @MethodSource("resolve")
    void resolve(String value, String pack, String file) {
        when(element.getAnnotation(Source.class)).thenReturn(new StubSource(value));
        
        packager.resolve(element);
        
        verifyNoInteractions(environment);
        
        assertEquals(element, packager.element());
        assertEquals(pack, packager.folder());
        assertEquals(file, packager.file());
    }
    
    static Stream<Arguments> resolve() {
        return Stream.of(of(Source.RELATIVE_PACKAGE, "com.karuslabs.commands.aot", "Commands"),
            of("com.karuslabs.Test", "com.karuslabs", "Test"),
            of("Unnamed", "", "Unnamed")
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("resolve_errors_parameters")
    void resolve_errors(String value, String error) {
        when(element.getAnnotation(Source.class)).thenReturn(new StubSource(value));
        
        packager.resolve(element);
        
        verify(environment).error(element, error);
        
        assertEquals("", packager.folder());
        assertEquals("Commands", packager.file());
    }
    
    static Stream<Arguments> resolve_errors_parameters() {
        return Stream.of(
            of("something.java", "File ends with \".java\", should not end with file extension"),
            of("something.class", "File ends with \".class\", should not end with file extension"),
            of("_", "Invalid package name")
        );
    }

}


class StubSource implements Source {

    private String value;
    
    
    StubSource(String value) {
        this.value = value;
    }
    
    
    @Override
    public String value() {
        return value;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Source.class;
    }
    
}

class StubName implements Name {
    
    private String value;
    
    
    StubName(String value) {
        this.value = value;
    }
    

    @Override
    public boolean contentEquals(CharSequence cs) {
        return value.contains(cs);
    }

    @Override
    public int length() {
        return value.length();
    }

    @Override
    public char charAt(int index) {
        return value.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return value.subSequence(start, end);
    }
    
    
    @Override
    public String toString() {
        return value;
    }
    
}
