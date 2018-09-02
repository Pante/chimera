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
package com.karuslabs.commons.codec.encoder.encoded;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;

import com.karuslabs.commons.codec.encoder.Encoded;

import java.util.regex.Pattern;


public abstract class EncodedStringifiedPrimitive<T, R extends JsonNode> implements Encoded<T, R> {
    
    private static final LenientStringifiedPrimitive LENIENT = new LenientStringifiedPrimitive();
    private static final StrictStringifiedPrimitive STRICT = new StrictStringifiedPrimitive();
    
    
    public static LenientStringifiedPrimitive lenient() {
        return LENIENT;
    }
    
    public static StrictStringifiedPrimitive unstringify() {
        return STRICT;
    }
    
    
    private static final Pattern NUMBER = Pattern.compile("^(-?)(0|([1-9][0-9]*))(\\\\.[0-9]+)?$");
    
    
    protected ValueNode unstringify(JsonNodeFactory factory, String value) {
        if (value.equals("true") || value.equals("false"))
            return factory.booleanNode(Boolean.parseBoolean(value));
            
        if (NUMBER.matcher(value).matches()) {
            return factory.numberNode(Double.parseDouble(value));
            
        } else {
            return factory.textNode(value);
        }
    }
    
    
    public static class StrictStringifiedPrimitive extends EncodedStringifiedPrimitive<String, ValueNode> {

        @Override
        public ValueNode encode(JsonNodeFactory factory, String value) {
            return unstringify(factory, value);
        }
        
    }
    
    public static class LenientStringifiedPrimitive extends EncodedStringifiedPrimitive<Object, JsonNode> {

        @Override
        public JsonNode encode(JsonNodeFactory factory, Object value) {
            if (value instanceof String[]) {
                var array = factory.arrayNode();
                for (var string : (String[]) value) {
                    array.add(unstringify(factory, string));
                }
                
                return array;
                
            } else {
                return unstringify(factory, (String) value);
            }
        }
        
    }
    
}
