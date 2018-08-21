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

import java.io.File;
import java.util.Map;


public @Static class Configuration {
    
    static final ObjectMapper MAPPER = new ObjectMapper();
    
    public static Map<String, String> from(File file) {
        var name = file.getName();
        switch (name.substring(name.lastIndexOf('.'))) {
            case ".yml":
            case ".yaml":
            case ".json":
            case ".properties":
            default:
                throw new UnsupportedOperationException("Unuspported file extension");
        }
    }
    
    static void visit(Map<String, String> map, JsonNode node, String path) {
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
    }
    
}
