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
package com.karuslabs.commons.codec.decoders;

import com.fasterxml.jackson.databind.node.*;

import java.util.Map;


public class Stringifier extends StringKeyDecoder<String> {
    
    private static final Stringifier STRINGIFIER = new Stringifier();
    
    
    public static Stringifier stringify() {
        return STRINGIFIER;
    }
    
    
    public Stringifier() {
        super(null);
    }
    
    
    @Override
    public Map<String, String> visit(String path, ArrayNode array, Map<String, String> map) {
        for (int i = 0; i < array.size(); i++) {
            visit(path + "[" + i + "]", array.get(i), map);
        }
        
        return map;
    }
    
    @Override
    public Map<String, String> visit(String path, ValueNode node, Map<String, String> map) {
        map.put(path, node.asText());
        return map;
    }
    
}
