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

import static com.karuslabs.smoke.Format.format;
import static javax.tools.Diagnostic.Kind.*;

public class Log {

    private final Messager messager;
    private boolean error;
    
    public Log(Messager messager) {
        this.messager = messager;
        this.error = false;
    }
    
    
    public void error(@Nullable Element location, Object value, String reason, String resolution) {
        error(location, format(value, reason, resolution));
    }
    
    public void error(@Nullable Element location, Object value, String reason) {
        error(location, format(value, reason));
    }
    
    public void error(@Nullable Element location, String message) {
        print(location, ERROR, message);
        error = true;
    }
    
    
    public void warn(@Nullable Element location, Object value, String reason, String resolution) {
        warn(location, format(value, reason, resolution));
    }
    
    public void warn(@Nullable Element location, Object value, String reason) {
        warn(location, format(value, reason));
    }
    
    public void warn(@Nullable Element location, String message) {
        print(location, WARNING, message);
    }
    
    
    public void note(@Nullable Element location, Object value, String reason, String resolution) {
        note(location, format(value, reason, resolution));
    }
    
    public void note(@Nullable Element location, Object value, String reason) {
        note(location, format(value, reason));
    }
    
    public void note(@Nullable Element location, String message) {
        print(location, NOTE, message);
    }
    
    
    private void print(@Nullable Element location, Kind kind, String message) {
        if (location != null) {
            messager.printMessage(kind, message, location);
            
        } else {
            messager.printMessage(kind, message);
        }
    }
    
    public void clear() {
        error = false;
    }
    
    public boolean error() {
        return error;
    }
    
}
