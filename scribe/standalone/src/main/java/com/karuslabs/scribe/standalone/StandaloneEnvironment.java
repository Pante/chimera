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
package com.karuslabs.scribe.standalone;

import com.karuslabs.scribe.core.*;
import javax.annotation.processing.Messager;

import javax.lang.model.element.Element;

import static javax.tools.Diagnostic.Kind.*;


public class StandaloneEnvironment extends Environment<Element> {
    
    Messager messager;
    
    
    public StandaloneEnvironment(Messager messager) {
        super(Project.EMPTY, Resolver.ELEMENT);
        this.messager = messager;
    }

    @Override
    public void error(String message) {
        messager.printMessage(ERROR, message);
    }

    @Override
    public void error(Element location, String message) {
        messager.printMessage(ERROR, message, location);
    }

    
    @Override
    public void warn(String message) {
        messager.printMessage(WARNING, message);
    }

    @Override
    public void warn(Element location, String message) {
        messager.printMessage(WARNING, message, location);
    }

}
