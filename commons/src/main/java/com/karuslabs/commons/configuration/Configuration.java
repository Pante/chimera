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
package com.karuslabs.commons.configuration;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.*;
import java.util.*;


public abstract class Configuration<T> {
        
    static final ObjectMapper JSON = new ObjectMapper();
    static final ObjectMapper PROPERTIES = new JavaPropsMapper();
    static final ObjectMapper YAML = new ObjectMapper(new YAMLFactory());
    
    
    public Map<String, T> from(File file) {
        var name = file.getName();
        var extension = name.substring(name.lastIndexOf('.'));
        switch (extension) {
            case ".json":
                return from(file, JSON);

            case ".properties":
                return from(file, PROPERTIES);

            case ".yml":
            case ".yaml":
                return from(file, YAML);

            default:
                throw new UnsupportedOperationException("Unuspported file extension: " + extension);
        }
    }

    Map<String, T> from(File file, ObjectMapper mapper) {
        try {
            return visit("", mapper.readTree(file), new HashMap<>());

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Map<String, T> fromJSON(InputStream stream) {
        return from(stream, JSON);
    }

    public Map<String, T> fromProperties(InputStream stream) {
        return from(stream, PROPERTIES);
    }

    public Map<String, T> fromYAML(InputStream stream) {
        return from(stream, YAML);
    }

    Map<String, T> from(InputStream stream, ObjectMapper mapper) {
        try {
            return visit("", mapper.readTree(stream), new HashMap<>());

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    
    Map<String, T> visit(String path, JsonNode node, Map<String, T> map) {
        if (node.isObject()) {
            var object = (ObjectNode) node;
            var prefix = path.isEmpty() ? "" : path + ".";

            var fields = object.fields();
            while (fields.hasNext()) {
                var entry = fields.next();
                visit(prefix + entry.getKey(), entry.getValue(), map);
            }

        } else if (node.isArray()) {
            visitArray(path, (ArrayNode) node, map);

        } else {
            visitValue(path, (ValueNode) node, map);
        }

        return map;
    }
    
    protected abstract void visitArray(String path, ArrayNode array, Map<String, T> map);
    
    protected abstract void visitValue(String path, ValueNode value, Map<String, T> map);
    
}
