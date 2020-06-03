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

import com.karuslabs.scribe.core.YAML;

import java.io.*;

import javax.annotation.processing.*;
import javax.tools.StandardLocation;

import static javax.tools.Diagnostic.Kind.ERROR;


public class StandaloneYAML extends YAML {

    Filer filer;
    Messager messager;
    
    
    public StandaloneYAML(Filer filer, Messager messager) {
        super("Scribe Standalone");
        this.filer = filer;
        this.messager = messager;
    }
    
    
    @Override
    protected Writer writer() throws IOException {
        return filer.createResource(StandardLocation.CLASS_OUTPUT, "", "plugin.yml").openWriter();
    }

    @Override
    protected void handle(IOException e) {
        messager.printMessage(ERROR, "Failed to create plugin.yml");
        messager.printMessage(ERROR, e.getMessage());
    }

}
