/*
 * The MIT License
 *
 * Copyright 2019 Matthias.
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
package com.karuslabs.annotations.processors;

import java.lang.annotation.Annotation;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
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
                return Set.of(single);

            } else if (element == namespaces) {
                return Set.of(multiple);

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
    void annotated() {
        assertEquals(Elements.annotated(Set.of(namespace, namespaces), environment), Set.of(single, multiple));
    }
    
}
