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

package com.karuslabs.scribe.core;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Map;

import org.snakeyaml.engine.v2.api.*;
import org.snakeyaml.engine.v2.common.*;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;


public class YAMLWriter {

    String type;
    File file;
    Dump dump;
    
    
    public YAMLWriter(String type, File file) {
        this(type, file, new Dump(DumpSettings.builder().setDefaultFlowStyle(FlowStyle.BLOCK).setDefaultScalarStyle(ScalarStyle.PLAIN).build()));
    }
    
    public YAMLWriter(String type, File file, Dump dump) {
        this.type = type;
        this.file = file;
        this.dump = dump;
    }
    
    
    public void write(Map<String, Object> map) throws IOException {
        try (var writer = new BufferedWriter(new FileWriter(file))) {
            writer.append("# This file was generated using Scribe " + type + " 4.5.0 at: " + LocalDateTime.now().format(ISO_DATE_TIME) + "\n")
                  .append(dump.dumpToString(map));
        }
    }
    
}
