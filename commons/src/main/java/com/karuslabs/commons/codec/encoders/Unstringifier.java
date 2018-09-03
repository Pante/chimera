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
package com.karuslabs.commons.codec.encoders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;

import com.karuslabs.commons.codec.encoder.*;
import com.karuslabs.commons.codec.encoder.encoded.StringifiedPrimitive;
import com.karuslabs.commons.codec.nodes.SparseArrayNode;

import java.util.Map;
import java.util.regex.Pattern;


public class Unstringifier<T> extends Encoder<Map<String, T>, JsonNode> {
    
    private static final Unstringifier<Object> LENIENT = new Unstringifier(StringifiedPrimitive.lenient());
    private static final Unstringifier<String> UNSTRINGIFY = new Unstringifier(StringifiedPrimitive.unstringify());
    
    
    public static Unstringifier<Object> lenient() {
        return LENIENT;
    }
    
    public static Unstringifier<String> unstringify() {
        return UNSTRINGIFY;
    }
            
        
    private static final Pattern SEPERATOR = Pattern.compile("([\\.]|((?<=[\\[][0-9][\\]])|(?=[\\[][0-9][\\]])))");
    
    
    private Encoded<T, JsonNode> encoder;
    
    
    public Unstringifier(Encoded<T, JsonNode> value) {
        this.encoder = value;
    }
    
    
    @Override
    public JsonNode encode(JsonNodeFactory factory, Map<String, T> map) {
        JsonNode root = null;
        for (var entry : map.entrySet()) {
            root = traverse(root, factory, SEPERATOR.split(entry.getKey()), entry.getValue());
        }
        
        return root;
    }
    
    protected JsonNode traverse(JsonNode root, JsonNodeFactory factory, String[] names, T value) {
        if (root ==  null) {
            root = node(factory, parse(names[0]));
        }
        JsonNode current = root;
        
        int index = -1;
        for (int i = 0; i < names.length - 1; i++) {
            var name = names[i];
            int ahead = parse(names[i + 1]);
            
            if (current instanceof ObjectNode) {
                var object = (ObjectNode) current;
                current = object.get(name);
                if (current == null) {
                    object.set(name, current = node(factory, ahead));
                }
                
            } else if (current instanceof ArrayNode) {
                var array = (ArrayNode) current;
                current = array.get(index);
                if (current instanceof NullNode) {
                    array.set(index, current = node(factory, ahead));
                }
            }
            
            index = ahead;
        }
        
        encode(current, factory, names[names.length - 1], index, value);
        return root;
    }        
    
    protected int parse(String name) {
        int first = name.indexOf('[') + 1;
        int last = name.indexOf(']');
        return first == 1 && last != -1 ? Integer.parseInt(name.substring(first, last)) : - 1;
    }
    
    protected JsonNode node(JsonNodeFactory factory, int index) {
        return index == -1 ? factory.objectNode() : new SparseArrayNode(factory);
    }
    
    protected void encode(JsonNode node, JsonNodeFactory factory, String name, int index, T value) {
        if (node instanceof ObjectNode) {
            ((ObjectNode) node).set(name, encoder.encode(factory, value));
            
        } else if (node instanceof ArrayNode) {
            ((ArrayNode) node).set(index, encoder.encode(factory, value));
        }
    }
    
}
