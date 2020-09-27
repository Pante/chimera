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
package com.karuslabs.commons.command.aot;

import java.util.Set;
import javax.lang.model.element.Element;

import org.junit.jupiter.api.*;

import static org.mockito.Mockito.*;


class AnalyzerTest {
    
    Environment environment = spy(new Environment(null, null, null, null));
    Analyzer analyzer = new Analyzer(environment);
    Identifier argument = Identifier.argument(mock(Element.class), "argument", "argument");
    Identifier invalid = Identifier.argument(mock(Element.class), "<invalid>", "invalid");
    Identifier literal = Identifier.literal(mock(Element.class), "literal", "literal", Set.of());
    
    
    @BeforeEach
    void before() {
        argument.bind(environment, Binding.TYPE, mock(Element.class));
        
        literal.add(environment, argument);
        literal.add(environment, invalid);
        
        environment.scopes.put(mock(Element.class), Identifier.root().add(environment, literal));
        
        doNothing().when(environment).error(any(), any());
    }
    
    
    @Test
    void analyze() {
        analyzer.analyze();
        
        verify(environment).error(invalid.location, "\"<invalid>\" is an invalid argument, an ArgumentType<?> should be bound to it");
        verifyNoMoreInteractions(environment);
    }

} 
