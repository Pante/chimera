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
package com.karuslabs.commons.io.generator.generators;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;

import com.karuslabs.commons.io.generator.*;
import com.karuslabs.commons.io.jackson.SparseArrayNode;

import java.util.Map;
import java.util.regex.Pattern;


public class Unstringifier<T> extends Generator<Map<String, T>, JsonNode> {
    
    private static final Pattern SEPERATOR = Pattern.compile("([\\.]|((?<=[\\[][0-9][\\]])|(?=[\\[][0-9][\\]])))");
    
    private Value<T, JsonNode> value;
    private String seperator;
    
    
    public Unstringifier(Value<T, JsonNode> value, String seperator) {
        this.value = value;
        this.seperator = seperator;
    }
    
    
    @Override
    public JsonNode generate(JsonNodeFactory factory, Map<String, T> map) {
        var root = factory.objectNode();
        for (var entry : map.entrySet()) {
            generate(root, factory, SEPERATOR.split(entry.getKey()), -1, entry.getValue());
        }
        
        return root;
    }
    
    protected void generate(ObjectNode parent, JsonNodeFactory factory, String[] names, int index, T value) {
        var current = parent;
        int last = names.length - 1;
        
        for (index += 1; index <= last; index++) {
            var name = names[index];
            
            if (index == last) {
                parent.set(name, this.value.generate(factory, value));
                return;
            }
            
            var next = names[index + 1];
            var child = current.get(name);
            int parsed = index(next);
            
            if (parsed == -1) {
                if (child == null) {
                    child = factory.objectNode();
                }
                
                current.set(name, child);
                current = (ObjectNode) child;

            } else {
                generate(child == null ?  new SparseArrayNode(factory) : (ArrayNode) child, factory, names, index, value, parsed);
                return;
            }
        }
    }
    
    protected void generate(ArrayNode parent, JsonNodeFactory factory, String[] names, int index, T value, int name) {
        var current = parent;
        int last = names.length - 1;
        
        for (index += 1; index <= last; index++) {
            if (index == last) {
                parent.set(name, this.value.generate(factory, value));
                return;
            }
            
            var next = names[index + 1];
            var child = current.get(name);
            int parsed = index(next);
            
            if (parsed != -1) {
                if (child == null) {
                    child = new SparseArrayNode(factory);
                }
                current.set(name, child);
                current = (ArrayNode) child;
                
            } else {
                generate(child == null ? factory.objectNode() : (ObjectNode) child, factory, names, index, value);
                return;
            }
        }
    }

        
    
    protected int index(String name) {
        int first = name.indexOf('[') + 1;
        int last = name.indexOf(']');
        return first != -1 && last != -1 ? Integer.parseInt(name.substring(first, last)) : - 1;
    }
    
}
