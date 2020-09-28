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
package com.karuslabs.annotations.processor;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;
import java.util.function.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static com.karuslabs.annotations.processor.Messages.format;
import static javax.tools.Diagnostic.Kind.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;

class LoggerTest {
    
    static final Element ELEMENT = mock(Element.class);
    static final Messager MESSAGER = mock(Messager.class);
    static final Logger LOGGER = new Logger(MESSAGER).zone(ELEMENT);
    
    Element element = mock(Element.class);
    Messager messager = mock(Messager.class);
    Logger logger = new Logger(messager).zone(element);
    
    @ParameterizedTest
    @MethodSource("log_value_reason_resolution_parameters")
    void log_value_reason_resolution(Log log, Kind kind) {
        log.accept(1, "reason", "resolution");
        
        verify(MESSAGER).printMessage(kind, format(1, "reason", "resolution"), ELEMENT);
        reset(MESSAGER);
    }
    
    static Stream<Arguments> log_value_reason_resolution_parameters() {
        return Stream.of(
            of((Log) LOGGER::error, ERROR),
            of((Log) LOGGER::warn, WARNING),
            of((Log) LOGGER::note, NOTE)
        );
    }
    
    @ParameterizedTest
    @MethodSource("log_value_reason_parameters")
    void log_value_reason(BiConsumer<Object, String> consumer, Kind kind) {
        consumer.accept(1, "reason");
        
        verify(MESSAGER).printMessage(kind, format(1, "reason"), ELEMENT);
        reset(MESSAGER);
    }
    
    static Stream<Arguments> log_value_reason_parameters() {
        return Stream.of(
            of((BiConsumer<Object, String>) LOGGER::error, ERROR),
            of((BiConsumer<Object, String>) LOGGER::warn, WARNING),
            of((BiConsumer<Object, String>) LOGGER::note, NOTE)
        );
    }
    
    @ParameterizedTest
    @MethodSource("log_message_parameters")
    void log_value(Consumer<String> consumer, Kind kind) {
        consumer.accept("message");
        
        verify(MESSAGER).printMessage(kind, "message", ELEMENT);
        reset(MESSAGER);
    }
    
    static Stream<Arguments> log_message_parameters() {
        return Stream.of(
            of((Consumer<String>) LOGGER::error, ERROR),
            of((Consumer<String>) LOGGER::warn, WARNING),
            of((Consumer<String>) LOGGER::note, NOTE)
        );
    }
    
    
    @Test
    void zone() {
        logger.zone(null).error("message");
        
        assertTrue(logger.error());
        verify(messager).printMessage(ERROR, "message");
    }
    
    @Test
    void clear() {
        logger.error("first");
        assertTrue(logger.error());
        
        logger.clear();
        
        logger.warn("second");
        assertFalse(logger.error());
        verify(messager).printMessage(WARNING, "second");
    }
    
}

@FunctionalInterface
interface Log {
    void accept(Object value, String reason, String resolution);
}
