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
package com.karuslabs.scribe.standalone.resolvers;

import java.util.*;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static javax.tools.Diagnostic.Kind.ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class SingleResolverTest {
    
    Messager messager = mock(Messager.class);
    SingleResolver resolver = new StubResolver(messager);
    Element first = mock(Element.class);
    Element second = mock(Element.class);
    
    
    @Test
    void check_single() {
        assertTrue(resolver.check(Set.of(first)));
        verifyNoInteractions(messager);
    }
    
    
    @Test
    void check_multiple() {
        assertFalse(resolver.check(Set.of(first, second)));
        
        verify(messager).printMessage(ERROR, "Invalid number of @Something annotations, plugin must contain only one @Something annotation", first);
        verify(messager).printMessage(ERROR, "Invalid number of @Something annotations, plugin must contain only one @Something annotation", second);
    }

}

class StubResolver extends SingleResolver {

    public StubResolver(Messager messager) {
        super(messager, "Something");
    }

    @Override
    protected void resolve(Element element, Map<String, Object> results) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
