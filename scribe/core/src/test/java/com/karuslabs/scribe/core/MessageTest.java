/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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
package com.karuslabs.scribe.core;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;


@ExtendWith(MockitoExtension.class)
class MessageTest {
    
    @ParameterizedTest
    @MethodSource("message_parameters")
    void message(Message<String> message, Message.Type type) {
        assertEquals(message.location, "location");
        assertEquals(message.type, type);
        assertEquals(message.value, "value");
    }
    
    static Stream<Arguments> message_parameters() {
        return Stream.of(
            of(Message.info("location", "value"), Message.Type.INFO),
            of(Message.warning("location", "value"), Message.Type.WARNING),
            of(Message.error("location", "value"), Message.Type.ERROR)
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("equality_parameters")
    void equality(Object other, boolean expected) {
        var message = Message.error("location", "value");
        
        assertEquals(message.equals(other), expected);
        assertEquals(message.hashCode() == other.hashCode(), expected);
    }
    
    static Stream<Arguments> equality_parameters() {
        return Stream.of(
            of(Message.error("location", "value"), true),
            of(Message.error("l", "value"), false),
            of(Message.error("location", "v"), false),
            of(Message.warning("location", "value"), false),
            of(new Object(), false)
        );
    }
    
    
    @Test
    void toString_equals() {
        assertEquals(
            Message.error(Object.class, "this is a message").toString(),
            "[ERROR] class java.lang.Object: this is a message"
        );
    }
    
} 
