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
package com.karuslabs.commons.io.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;

import org.checkerframework.checker.nullness.qual.Nullable;


public class SimpleVisitor<T, R> implements Visitor<T, R> {
    
    protected R value;

    
    public SimpleVisitor(R value) {
        this.value = value;
    }

    
    @Override
    public R visitArray(String path, ArrayNode array, T value) {
        return visitDefault(path, array, value);
    }

    @Override
    public R visitObject(String path, ObjectNode object, T value) {
        return visitDefault(path, object, value);
    }

    
    @Override
    public R visitBinary(String path, BinaryNode binary, T value) {
        return visitDefault(path, binary, value);
    }

    @Override
    public R visitBoolean(String path, BooleanNode bool, T value) {
        return visitDefault(path, bool, value);
    }

    @Override
    public R visitMissing(String path, MissingNode missing, T value) {
        return visitDefault(path, missing, value);
    }

    @Override
    public R visitNull(String path, NullNode nil, T value) {
        return visitDefault(path, nil, value);
    }

    @Override
    public R visitNumber(String path, NumericNode number, T value) {
        return visitDefault(path, number, value);
    }

    @Override
    public R visitPOJO(String path, POJONode pojo, T value) {
        return visitDefault(path, pojo, value);
    }

    @Override
    public R visitText(String path, TextNode text, T value) {
        return visitDefault(path, text, value);
    }
    
    protected @Nullable R visitDefault(String path, JsonNode node, T value) {
        return this.value;
    }
    
}
