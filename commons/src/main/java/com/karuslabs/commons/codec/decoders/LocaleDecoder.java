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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;

import com.karuslabs.commons.codec.decoder.Decoder;
import com.karuslabs.commons.locale.Locales;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LocaleDecoder extends Decoder<Map<UUID, Locale>, Map<UUID, Locale>> {
    
    private static final LocaleDecoder DECODER = new LocaleDecoder();
    
    
    public static LocaleDecoder decode() {
        return DECODER;
    }
    
    
    public LocaleDecoder() {
        this(Map.of());
    }
    
    public LocaleDecoder(Map<UUID, Locale> map) {
        super(map);
    }

    @Override
    public Map<UUID, Locale> visit(String path, ObjectNode node, Map<UUID, Locale> map) {
        var fields = node.fields();
        while (fields.hasNext()) {
            var entry = fields.next();
            visit(entry.getKey(), entry.getValue(), map);
        }

        return map;
    }

    @Override
    public Map<UUID, Locale> visit(String uuid, ValueNode locale, Map<UUID, Locale> map) {
        map.put(UUID.fromString(uuid), Locales.of(locale.asText()));
        return map;
    }
    
    @Override
    public Map<UUID, Locale> visitDefault(String uuid, JsonNode locale, Map<UUID, Locale> map) {
        throw new IllegalArgumentException("Invalid value for key: " + uuid);
    }

    @Override
    protected Map<UUID, Locale> value() {
        return new ConcurrentHashMap<>();
    }

}
