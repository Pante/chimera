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
package com.karuslabs.annotations.processor;

import java.util.stream.Stream;
import javax.lang.model.element.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


class FilterTest {
    
    static final Element MODULE;
    static final Element PACKAGE;
    static final Element TYPE;
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
        
        DEFAULT = when(mock(VariableElement.class).accept(any(), any())).then(
            invocation -> invocation.getArgument(0, ElementVisitor.class).visitVariable((VariableElement) invocation.getMock(), null)
        ).getMock();
        
        ENCLOSED = when(mock(VariableElement.class).accept(any(), any())).then(
            invocation -> invocation.getArgument(0, ElementVisitor.class).visitVariable((VariableElement) invocation.getMock(), null)
        ).getMock();
        
        when(ENCLOSED.getEnclosingElement()).thenReturn(MODULE);
    }
    
    
    @ParameterizedTest
    @MethodSource("filters")
    void visit(ElementVisitor<Element, Void> visitor, Element element, Element expected) {
        assertEquals(expected, element.accept(visitor, null));
    }
    
    
    static Stream<Arguments> filters() {
        return Stream.of(
            of(Filter.CLASS, MODULE, null),
            of(Filter.CLASS, PACKAGE, null),
            of(Filter.CLASS, TYPE, TYPE),
            of(Filter.CLASS, DEFAULT, null),
            of(Filter.CLASS, ENCLOSED, null),
            
            of(Filter.PACKAGE, MODULE, null),
            of(Filter.PACKAGE, PACKAGE, PACKAGE),
            of(Filter.PACKAGE, TYPE, null),
            of(Filter.PACKAGE, ENCLOSED, null),
            
            of(Filter.MODULE, MODULE, MODULE),
            of(Filter.MODULE, PACKAGE, null),
            of(Filter.MODULE, TYPE, null),
            of(Filter.MODULE, ENCLOSED, MODULE)
        );
    }

} 
