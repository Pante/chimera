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
package com.karuslabs.commons.codec.encoder;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.*;


public abstract class Encoder<T, R extends JsonNode> implements Encoded<T, R> {
    
    protected static final ObjectWriter JSON = new ObjectMapper().writer().with(new DefaultPrettyPrinter());
    protected static final ObjectWriter PROPERTIES = new JavaPropsMapper().writer().with(new DefaultPrettyPrinter());
    protected static final ObjectWriter YAML = new ObjectMapper(new YAMLFactory()).writer().with(new DefaultPrettyPrinter());
    
    
    public void to(File file, T value) {
        try {
            var name = file.getName();
            var format = name.substring(name.lastIndexOf('.') + 1);
            var writer = writer(format);
            writer.writeValue(file, encode(JsonNodeFactory.instance, value));

        } catch (IOException e) {
            exceptional(e);
        }
    }

    public void to(OutputStream stream, String format, T value) {
        try (stream) {
            var writer = writer(format);
            writer.writeValue(stream, encode(JsonNodeFactory.instance, value));

        } catch (IOException e) {
            exceptional(e);
        }
    }
    
    protected ObjectWriter writer(String format) {
        switch (format) {
            case "json":
                return JSON;

            case "properties":
                return PROPERTIES;

            case "yml":
            case "yaml":
                return YAML;

            default:
                return defaultWriter(format);
        }
    }

    protected ObjectWriter defaultWriter(String format) {
        throw new UnsupportedOperationException("Unsupported format: " + format);
    }

    protected void exceptional(IOException e) {
        throw new UncheckedIOException(e);
    }
    
}
