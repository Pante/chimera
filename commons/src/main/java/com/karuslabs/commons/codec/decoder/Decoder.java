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
package com.karuslabs.commons.codec.decoder;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.*;


public abstract class Decoder<T, R> extends SimpleVisitor<T, R> {
    
    protected static final ObjectMapper JSON = new ObjectMapper();
    protected static final ObjectMapper PROPERTIES = new JavaPropsMapper();
    protected static final ObjectMapper YAML = new ObjectMapper(new YAMLFactory());

    
    public Decoder(R value) {
        super(value);
    }
    
    
    public R from(File file) {
        try {
            var name = file.getName();
            var format = name.substring(name.lastIndexOf('.') + 1);
            return visit("", mapper(format).readTree(file), value());

        } catch (IOException e) {
            return exceptional(e);
        }
    }

    public R from(InputStream stream, String format) {
        try (stream) {
            return visit("", mapper(format).readTree(stream), value());

        } catch (IOException e) {
            return exceptional(e);
        }
    }
    
    protected ObjectMapper mapper(String format) {
        switch (format) {
            case "json":
                return JSON;

            case "properties":
                return PROPERTIES;

            case "yml": case "yaml":
                return YAML;

            default:
                return defaultMapper(format);
        }
    }

    protected ObjectMapper defaultMapper(String format) {
        throw new UnsupportedOperationException("Unsupported format: " + format);
    }
        
    protected T value() {
        return null;
    }

    protected R exceptional(IOException e) {
        throw new UncheckedIOException(e);
    }
    
}
