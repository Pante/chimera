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
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import com.karuslabs.commons.codec.encoder.Encoder;
import com.karuslabs.commons.locale.Locales;

import java.util.*;


public class LocaleEncoder extends Encoder<Map<UUID, Locale>, JsonNode> {
    
    private static final LocaleEncoder GENERATOR = new LocaleEncoder();
    
    public static LocaleEncoder encode() {
        return GENERATOR;
    }
    
    
    @Override
    public JsonNode encode(JsonNodeFactory factory, Map<UUID, Locale> map) {
        var container = factory.objectNode();
        for (var entry : map.entrySet()) {
            container.set(entry.getKey().toString(), factory.textNode(Locales.of(entry.getValue())));
        }
        
        return container;
    }
    
}
