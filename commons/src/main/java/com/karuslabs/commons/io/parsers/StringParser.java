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
package com.karuslabs.commons.io.parsers;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import java.util.*;


public abstract class StringParser<T> extends Parser<Map<String, T>> {

    private static final LenientStringifier LENIENT = new LenientStringifier();
    private static final Stringifier FLAT = new Stringifier();

    
    public static LenientStringifier lenient() {
        return LENIENT;
    }
    
    public static final StringParser stringify() {
        return FLAT;
    }
    
    
    @Override
    protected Map<String, T> visit(String path, JsonNode node, Map<String, T> map) {
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

    
    @Override
    protected Map<String, T> initial() {
        return new HashMap<>();
    }
    
    
    public static class LenientStringifier extends StringParser<Object> {

        private static final String[] EMPTY = new String[0];

        @Override
        protected void visitArray(String path, ArrayNode array, Map<String, Object> map) {
            if (array.size() == 0) {
                map.put(path, EMPTY);
                return;
            }

            var strings = new ArrayList<String>(array.size());
            for (int i = 0; i < array.size(); i++) {
                var value = array.get(i);
                if (value.isArray() || value.isObject()) {
                    visit(path + "[" + i + "]", value, map);

                } else {
                    strings.add(value.asText());
                }
            }

            if (!strings.isEmpty()) {
                map.put(path, strings.toArray(EMPTY));
            }
        }

        @Override
        protected void visitValue(String path, ValueNode value, Map<String, Object> map) {
            map.put(path, value.asText());
        }

    }
    
    public static class Stringifier extends StringParser<String> {

        @Override
        protected void visitArray(String path, ArrayNode array, Map<String, String> map) {
            for (int i = 0; i < array.size(); i++) {
                visit(path + "[" + i + "]", array.get(i), map);
            }
        }

        @Override
        protected void visitValue(String path, ValueNode value, Map<String, String> map) {
            map.put(path, value.asText());
        }

    }
    
}
