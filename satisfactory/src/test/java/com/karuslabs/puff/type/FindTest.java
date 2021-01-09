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
package com.karuslabs.Satisfactory.type;

import java.util.stream.Stream;
import javax.lang.model.element.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;

class FindTest {
    
    static final Element MODULE;
    static final Element PACKAGE;
    static final Element TYPE;
    static final Element EXECUTABLE;
    static final Element DEFAULT;
    static final Element ENCLOSED;
    
    static {
        MODULE = when(mock(ModuleElement.class).accept(any(), any())).then(
            invocation -> invocation.getArgument(0, ElementVisitor.class).visitModule((ModuleElement) invocation.getMock(), null)
        ).getMock();
        PACKAGE = when(mock(PackageElement.class).accept(any(), any())).then(
            invocation -> invocation.getArgument(0, ElementVisitor.class).visitPackage((PackageElement) invocation.getMock(), null)
        ).getMock();
        TYPE = when(mock(TypeElement.class).accept(any(), any())).then(
            invocation -> invocation.getArgument(0, ElementVisitor.class).visitType((TypeElement) invocation.getMock(), null)
        ).getMock();
        
        EXECUTABLE = when(mock(ExecutableElement.class).accept(any(), any())).then(
            invocation -> invocation.getArgument(0, ElementVisitor.class).visitExecutable((ExecutableElement) invocation.getMock(), null)
        ).getMock();
        
        DEFAULT = when(mock(VariableElement.class).accept(any(), any())).then(
            invocation -> invocation.getArgument(0, ElementVisitor.class).visitVariable((VariableElement) invocation.getMock(), null)
        ).getMock();
        
        ENCLOSED = when(mock(VariableElement.class).accept(any(), any())).then(
            invocation -> invocation.getArgument(0, ElementVisitor.class).visitVariable((VariableElement) invocation.getMock(), null)
        ).getMock();
        
        when(ENCLOSED.getEnclosingElement()).thenReturn(MODULE);
    }
    
    
    @ParameterizedTest
    @MethodSource("finders")
    void visit(ElementVisitor<Element, Void> visitor, Element element, Element expected) {
        assertEquals(expected, element.accept(visitor, null));
    }
    
    static Stream<Arguments> finders() {
        return Stream.of(
            of(Find.EXECUTABLE, MODULE, null),
            of(Find.EXECUTABLE, PACKAGE, null),
            of(Find.EXECUTABLE, TYPE, null),
            of(Find.EXECUTABLE, EXECUTABLE, EXECUTABLE),
            of(Find.EXECUTABLE, DEFAULT, null),
            of(Find.EXECUTABLE, ENCLOSED, null),
            
            of(Find.TYPE, MODULE, null),
            of(Find.TYPE, PACKAGE, null),
            of(Find.TYPE, TYPE, TYPE),
            of(Find.TYPE, DEFAULT, null),
            of(Find.TYPE, ENCLOSED, null),
            
            of(Find.PACKAGE, MODULE, null),
            of(Find.PACKAGE, PACKAGE, PACKAGE),
            of(Find.PACKAGE, TYPE, null),
            of(Find.PACKAGE, ENCLOSED, null),
            
            of(Find.MODULE, MODULE, MODULE),
            of(Find.MODULE, PACKAGE, null),
            of(Find.MODULE, TYPE, null),
            of(Find.MODULE, ENCLOSED, MODULE)
        );
    }

} 
