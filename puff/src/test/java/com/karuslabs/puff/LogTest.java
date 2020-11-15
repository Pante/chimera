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
package com.karuslabs.puff;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;
import java.util.function.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.checkerframework.checker.nullness.qual.Nullable;

import static com.karuslabs.puff.Texts.format;
import static javax.tools.Diagnostic.Kind.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;

class LogTest {
    
    static final Messager MESSAGER = mock(Messager.class);
    static final Log LOG = new Log(MESSAGER);
    
    Element element = mock(Element.class);
    Messager messager = mock(Messager.class);
    Log log = new Log(messager);
    
    @ParameterizedTest
    @MethodSource("log_value_reason_resolution_parameters")
    void log_value_reason_resolution(Printer consumer, Kind kind) {
        consumer.accept(element, 1, "reason", "resolution");
        
        verify(MESSAGER).printMessage(kind, format(1, "reason", "resolution"), element);
        reset(MESSAGER);
    }
    
    static Stream<Arguments> log_value_reason_resolution_parameters() {
        return Stream.of(of((Printer) LOG::error, ERROR),
            of((Printer) LOG::warn, WARNING),
            of((Printer) LOG::note, NOTE)
        );
    }
    
    @ParameterizedTest
    @MethodSource("log_value_reason_parameters")
    void log_value_reason(PartialPrinter printer, Kind kind) {
        printer.accept(element, 1, "reason");
        
        verify(MESSAGER).printMessage(kind, format(1, "reason"), element);
        reset(MESSAGER);
    }
    
    static Stream<Arguments> log_value_reason_parameters() {
        return Stream.of(of((PartialPrinter) LOG::error, ERROR),
            of((PartialPrinter) LOG::warn, WARNING),
            of((PartialPrinter) LOG::note, NOTE)
        );
    }
    
    @ParameterizedTest
    @MethodSource("log_message_parameters")
    void log_value(BiConsumer<Element, String> consumer, Kind kind) {
        consumer.accept(element, "message");
        
        verify(MESSAGER).printMessage(kind, "message", element);
        reset(MESSAGER);
    }
    
    static Stream<Arguments> log_message_parameters() {
        return Stream.of(of((BiConsumer<Element, String>) LOG::error, ERROR),
            of((BiConsumer<Element, String>) LOG::warn, WARNING),
            of((BiConsumer<Element, String>) LOG::note, NOTE)
        );
    }
    
    @Test
    void clear() {
        log.error(element, "first");
        assertTrue(log.error());
        
        log.clear();
        
        log.warn(element, "second");
        assertFalse(log.error());
        verify(messager).printMessage(WARNING, "second", element);
    }
    
}

@FunctionalInterface
interface Printer {
    void accept(@Nullable Element location, Object value, String reason, String resolution);
}

@FunctionalInterface
interface PartialPrinter {
    
    void accept(@Nullable Element location, Object value, String reason);
    
}