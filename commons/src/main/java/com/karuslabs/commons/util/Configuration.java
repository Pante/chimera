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
package com.karuslabs.commons.util;

import com.karuslabs.annotations.Static;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.*;
import java.util.*;


public @Static class Configuration {
    
    static final ObjectMapper JSON = new ObjectMapper();
    static final ObjectMapper PROPERTIES = new JavaPropsMapper();
    static final ObjectMapper YAML = new ObjectMapper(new YAMLFactory());
    
    
    public static Map<String, String> from(File file) {
        var name = file.getName();
        switch (name.substring(name.lastIndexOf('.'))) {
            case ".json":
                return from(file, JSON);
                
            case ".properties":
                return from(file, PROPERTIES);
                
            case ".yml":
            case ".yaml":
                return from(file, YAML);
                
            default:
                throw new UnsupportedOperationException("Unuspported file extension");
        }
    }
    
    static Map<String, String> from(File file, ObjectMapper mapper) {
        try {
            return visit(new HashMap<>(), mapper.readTree(file), "");
            
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    
    public static Map<String, String> fromJSON(InputStream stream) {
        return from(stream, JSON);
    }
    
    public static Map<String, String> fromProperties(InputStream stream) {
        return from(stream, PROPERTIES);
    }
    
    public static Map<String, String> fromYAML(InputStream stream) {
        return from(stream, YAML);
    }
    
    static Map<String, String> from(InputStream stream, ObjectMapper mapper) {
        try {
            return visit(new HashMap<>(), mapper.readTree(stream), "");
            
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    
    static Map<String, String> visit(Map<String, String> map, JsonNode node, String path) {
        if (node.isObject()) {
            var object = (ObjectNode) node;
            var prefix = path.isEmpty() ? "" : path + ".";
            
            var fields = object.fields();
            while (fields.hasNext()) {
                var entry = fields.next();
                visit(map, entry.getValue(), prefix + entry.getKey());
            }
            
        } else if (node.isArray()) {
            var array = (ArrayNode) node;
            for (int i = 0; i < array.size(); i++) {
                visit(map, array.get(i), path + "[" + i + "]");
            }
            
        } else {
            var value = (ValueNode) node;
            map.put(path, value.asText());
        }
        
        return map;
    }
    
}
