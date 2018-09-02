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

import com.fasterxml.jackson.databind.node.*;

import com.karuslabs.commons.util.Type;
import com.karuslabs.commons.codec.encoder.Encoded;


public class EncodedPrimitive implements Encoded<Object, ValueNode> {

    @Override
    public ValueNode encode(JsonNodeFactory factory, Object value) {
        switch (Type.of(value)) {
            case BOOLEAN:
                return factory.booleanNode((Boolean) value);
                
            case CHAR:
                return factory.textNode(Character.toString((Character) value));
                
            case STRING:
                return factory.textNode((String) value);
                  
            case BYTE:
                return factory.numberNode((Byte) value);
                
            case SHORT:
                return factory.numberNode((Short) value);
                
            case INT:
                return factory.numberNode((Integer) value);
                
            case LONG:
                return factory.numberNode((Long) value);
            
            case FLOAT:
                return factory.numberNode((Float) value);
                
            case DOUBLE:
                return factory.numberNode((Double) value);
                
            default:
                return exceptional(factory, value);
        }
    }
    
    protected ValueNode exceptional(JsonNodeFactory factory, Object value) {
        return factory.nullNode();
    }
    
}
