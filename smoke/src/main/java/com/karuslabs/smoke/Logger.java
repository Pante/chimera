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
package com.karuslabs.smoke;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

import org.checkerframework.checker.nullness.qual.Nullable;

import static com.karuslabs.smoke.Messages.format;
import static javax.tools.Diagnostic.Kind.*;

public class Logger {

    private final Messager messager;
    private @Nullable Element location;
    private boolean error;
    
    public Logger(Messager messager) {
        this.messager = messager;
        this.error = false;
    }

    
    public void error(Object value, String reason, String resolution) {
        error(format(value, reason, resolution));
    }
    
    public void error(Object value, String reason) {
        error(format(value, reason));
    }
    
    public void error(String message) {
        print(ERROR, message);
        error = true;
    }
    
    
    public void warn(Object value, String reason, String resolution) {
        warn(format(value, reason, resolution));
    }
    
    public void warn(Object value, String reason) {
        warn(format(value, reason));
    }
    
    public void warn(String message) {
        print(WARNING, message);
    }
    
    
    public void note(Object value, String reason, String resolution) {
        note(format(value, reason, resolution));
    }
    
    public void note(Object value, String reason) {
        note(format(value, reason));
    }
    
    public void note(String message) {
        print(NOTE, message);
    }
    
    
    
    private void print(Kind kind, String message) {
        if (location != null) {
            messager.printMessage(kind, message, location);
            
        } else {
            messager.printMessage(kind, message);
        }
    }

    
    public Logger zone(@Nullable Element location) {
        this.location = location;
        return this;
    }
    
    public void clear() {
        location = null;
        error = false;
    }
    
    public boolean error() {
        return error;
    }
    
}
