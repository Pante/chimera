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
package com.karuslabs.commons.codec.decoder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;


public interface Visitor<T, R> extends Decoded<T, R> {
    
    @Override
    public default R visit(String path, JsonNode node, T value) {
        switch (node.getNodeType()) {
            case ARRAY:
                return visit(path, (ArrayNode) node, value);
                
            case OBJECT:
                return visit(path, (ObjectNode) node, value);
                
            default:
                return visit(path, (ValueNode) node, value);
        }
    }
    
    public default R visit(String path, ValueNode node, T value) {
        switch (node.getNodeType()) {
            case BINARY:
                return visit(path, (BinaryNode) node, value);
                
            case BOOLEAN:
                return visit(path, (BooleanNode) node, value);
                
            case MISSING:
                return visit(path, (MissingNode) node, value);
                
            case NULL:
                return visit(path, (NullNode) node, value);
                
            case NUMBER:
                return visit(path, (NumericNode) node, value);
                
            case POJO:
                return visit(path, (POJONode) node, value);
                
            case STRING:
                return visit(path, (TextNode) node, value);
                
            default:
                throw new UnsupportedOperationException("Unsupported node: " + node.getNodeType().name());
        }
    }
    
    
    public R visit(String path, ArrayNode array, T value);
    
    public R visit(String path, ObjectNode object, T value);
    
    
    public R visit(String path, BinaryNode binary, T value);
    
    public R visit(String path, BooleanNode bool, T value);
    
    public R visit(String path, MissingNode missing, T value);
    
    public R visit(String path, NullNode nil, T value);
    
    public R visit(String path, NumericNode number, T value);
    
    public R visit(String path, POJONode pojo, T value);
    
    public R visit(String path, TextNode text, T value);
    
}
