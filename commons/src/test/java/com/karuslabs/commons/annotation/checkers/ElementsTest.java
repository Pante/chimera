/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
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
package com.karuslabs.commons.annotation.checkers;

import com.karuslabs.commons.annotation.JDK9;

import java.lang.annotation.Annotation;
import java.util.*;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;

import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;


class ElementsTest {
    
    TypeElement namespace = mock(TypeElement.class);
    TypeElement namespaces = mock(TypeElement.class);
    Element single = mock(Element.class);
    Element multiple = mock(Element.class);
    RoundEnvironment environment = new RoundEnvironment() {
        @Override
        public boolean processingOver() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean errorRaised() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Set<? extends Element> getRootElements() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Set<? extends Element> getElementsAnnotatedWith(TypeElement element) {
            if (element == namespace) {
                return singleton(single);
                
            } else if (element == namespaces) {
                return singleton(multiple);
                
            } else {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }

        @Override
        public Set<? extends Element> getElementsAnnotatedWith(Class<? extends Annotation> a) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
            
    };
    
    
    @Test
    @JDK9
    void annotated() {
        Set<TypeElement> elements = new HashSet<>(asList(namespace, namespaces));
        assertThat(Elements.annotated(elements, environment), equalTo(new HashSet<>(asList(single, multiple))));
    }
    
}
