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

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;


@ExtendWith(MockitoExtension.class)
class PrimitiveTest {
    
    Primitive primitive = new Primitive();
    
    
    @ParameterizedTest
    @MethodSource({"encode_provider"})
    void encode(Object decoded, Class<? extends ValueNode> expected) {
        assertEquals(expected, primitive.encode(JsonNodeFactory.instance, decoded).getClass());
    }
    
    static Stream<Arguments> encode_provider() {
        return Stream.of(
            of(true, BooleanNode.class), 
            of('a', TextNode.class), 
            of("b", TextNode.class), 
            of((byte) 0, IntNode.class), 
            of((short) 0, ShortNode.class), 
            of(0, IntNode.class),
            of(0L, LongNode.class), 
            of((float) 0, FloatNode.class),
            of((double) 0, DoubleNode.class),
            of(new Object(), NullNode.class)
        );
    }
    
}
