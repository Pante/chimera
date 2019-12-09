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
package com.karuslabs.scribe.standalone;

import java.io.IOException;
import java.time.LocalDateTime;
import javax.annotation.processing.*;
import javax.tools.*;

import org.snakeyaml.engine.v2.api.*;
import org.snakeyaml.engine.v2.common.*;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static javax.tools.Diagnostic.Kind.ERROR;


public class YAMLWriter {
    
    Filer filer;
    Messager messager;
    Dump dump;
    
    
    public YAMLWriter(Filer filer, Messager messager) {
        this(filer, messager, new Dump(DumpSettings.builder().setDefaultFlowStyle(FlowStyle.BLOCK).setDefaultScalarStyle(ScalarStyle.PLAIN).build()));
    }
    
    public YAMLWriter(Filer filer, Messager messager, Dump dump) {
        this.filer = filer;
        this.messager = messager;
        this.dump = dump;
    }
    
    
    public void write(Object object) {
        try (var writer = filer.createResource(StandardLocation.CLASS_OUTPUT, "", "plugin.yml").openWriter()) {
            writer.append("# This file was generated using Scribe Annotations 4.3.0 at: " + LocalDateTime.now().format(ISO_DATE_TIME) + "\n")
                  .append(dump.dumpToString(object));
            
        } catch (IOException e) {
            messager.printMessage(ERROR, "Failed to create plugin.yml");
            messager.printMessage(ERROR, e.getMessage());
        }
    }
            
}
