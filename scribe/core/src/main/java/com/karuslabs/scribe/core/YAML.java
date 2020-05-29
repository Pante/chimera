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


/**
 * A {@code YAML} serializes an object to a YAML file.
 */
public abstract class YAML {
    
    /**
     * Creates a {@code YAML} with the given project name that serializes objects to the given
     * file.
     * 
     * @param project the project name
     * @param file the file to which objects are serialized
     * @return a {@code YAML}
     */
    public static YAML fromFile(String project, File file) {
        return new FileYAML(project, file);
    }
    
    
    /**
     * The project name.
     */
    protected String name;
    /**
     * The dump used to serialize an object.
     */
    protected Dump dump;
    
    
    /**
     * Creates a {@code YAML} with the given project name.
     * 
     * @param project the project name
     */
    public YAML(String name) {
        this.name = name;
        dump = new Dump(DumpSettings.builder().setDefaultFlowStyle(FlowStyle.BLOCK).setDefaultScalarStyle(ScalarStyle.PLAIN).build());
    }
    
    
    public void write(Map<String, Object> mapping) {
        try (var writer = writer()) {
            writer.append("# This file was generated at " + LocalDateTime.now().format(ISO_DATE_TIME) + " using " + name + " 4.6.0 \n")
                  .append(dump.dumpToString(mapping));
            
        } catch (IOException e) {
            handle(e);
        }
    }
    
    /**
     * Returns a {@code Writer}.
     * 
     * @return a {@code Writer}
     * @throws IOException if the {@code Writer} could not be created
     */
    protected abstract Writer writer() throws IOException;
    
    /**
     * Handles the given {@code IOException}.
     * 
     * @param e the exception
     */
    protected abstract void handle(IOException e);
    
}

class FileYAML extends YAML {
    
    File file;

    
    FileYAML(String name, File file) {
        super(name);
        this.file = file;
    }

    
    @Override
    protected Writer writer() throws IOException {
        return new BufferedWriter(new FileWriter(file));
    }

    @Override
    protected void handle(IOException e) {
        throw new UncheckedIOException(e);
    }
    
}
