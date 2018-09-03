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

import com.karuslabs.commons.codec.nodes.SparseArrayNode;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class SimpleVisitorTest {
    
    private static final JsonNodeFactory factory = JsonNodeFactory.instance;
    
    
    @Spy SimpleVisitor<Object, Object> visitor = new SimpleVisitor(null) {};
    
    
    @ParameterizedTest
    @MethodSource({"visit_provider"})
    void visit(JsonNode node) {
        visitor.visit(null, node, null);
        
        verify(visitor).visit(null, node.getClass().cast(node), null);
        verify(visitor).visitDefault(null, node, null);
    }
    
    static Stream<JsonNode> visit_provider() {
        return Stream.of(
            new SparseArrayNode(factory),
            factory.objectNode(),
            factory.binaryNode(new byte[] {}),
            factory.booleanNode(true),
            MissingNode.getInstance(),
            factory.nullNode(),
            factory.numberNode(0),
            factory.pojoNode(null),
            factory.textNode("")
        );
    }
    
}
