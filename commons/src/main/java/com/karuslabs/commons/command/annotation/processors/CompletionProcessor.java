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
package com.karuslabs.commons.command.annotation.processors;

import com.karuslabs.annotations.processors.AnnotationProcessor;
import com.karuslabs.commons.command.annotation.*;

import java.util.*;
import javax.annotation.processing.*;
import javax.lang.model.element.*;


@SupportedAnnotationTypes({
    "com.karuslabs.commons.command.annotation.Literal", "com.karuslabs.commons.command.annotation.Literals",
    "com.karuslabs.commons.command.annotation.Registered", "com.karuslabs.commons.command.annotation.Registrations"
})
public class CompletionProcessor extends AnnotationProcessor {
    
    Set<Integer> indexes;
    
    
    public CompletionProcessor() {
        indexes = new HashSet<>();
    }

    
    @Override
    protected void process(Element element) {
        checkLiterals(element);
        checkRegistrations(element);
        indexes.clear();
    }
    
    protected void checkLiterals(Element element) {
        for (Literal literal : element.getAnnotationsByType(Literal.class)) {
            check(element, literal.index());
        }
    }
    
    protected void checkRegistrations(Element element) {
        for (Registered registered : element.getAnnotationsByType(Registered.class)) {
            check(element, registered.index());
        }
    }
    
    protected void check(Element element, int index) {
        if (index < 0) {
            error(element, "Index out of bounds: " + index + ", index must be equal to or greater than 0");
                    
        } else if (!indexes.add(index)) {
            error(element, "Conflicting indexes: " + index + ", indexes must be unique");
        }
    }
    
}
