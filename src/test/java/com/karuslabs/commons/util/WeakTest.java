/*
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
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
package com.karuslabs.commons.util;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


@TestInstance(PER_CLASS)
class WeakTest {
    
    static final String VALUE = "value";
    static Weak<String> value = new Weak<>(VALUE);
    static Weak<String> empty = new Weak<>(null);
    static Weak<String> other = new Weak<>(null);
    
    
    @Test
    void get() {
        assertEquals("value", value.get());
    }
    
    
    @Test
    void get_ThrowsException() {
        assertThrows(NoSuchElementException.class, empty::get);
    }
    
    
    @ParameterizedTest
    @MethodSource("ifPresent_parameters")
    void ifPresent(Weak<String> weak, int times) {
        Consumer<String> consumer = mock(Consumer.class);
        
        weak.ifPreset(consumer);
        
        verify(consumer, times(times)).accept(any());
    }
    
    static Stream<Arguments> ifPresent_parameters() {
        return Stream.of(of(value, 1), of(empty, 0));
    }
    
    
    @ParameterizedTest
    @MethodSource("ifPresentOrElse_parameters")
    void ifPresentOrElse(Weak<String> weak, int consumed, int ran) {
        Consumer<String> consumer = mock(Consumer.class);
        Runnable runnable = mock(Runnable.class);
        
        weak.ifPresentOrElse(consumer, runnable);
        
        verify(consumer, times(consumed)).accept(any());
        verify(runnable, times(ran)).run();
    }
    
    static Stream<Arguments> ifPresentOrElse_parameters() {
        return Stream.of(of(value, 1, 0), of(empty, 0, 1));
    }
    
    
    @ParameterizedTest
    @MethodSource("isPresent_parameters")
    void isPresent(Weak<String> weak, boolean expected) {
        assertEquals(expected, weak.isPresent());
    }
    
    static Stream<Arguments> isPresent_parameters() {
        return Stream.of(of(value, true), of(empty, false));
    }
    
    
    @ParameterizedTest
    @MethodSource("or_parameters")
    void or(Weak<String> weak, Weak<String> value, Weak<String> expected) {
        assertSame(expected, weak.or(() -> value));
    }
    
    static Stream<Arguments> or_parameters() {
        return Stream.of(of(value, other, value), of(empty, other, other));
    }
    
    
    @ParameterizedTest
    @MethodSource("orElse_parameters")
    void orElse(Weak<String> weak, String other, String expected) {
        assertEquals(expected, weak.orElse(other));
    }
    
    @ParameterizedTest
    @MethodSource("orElse_parameters")
    void orElseGet(Weak<String> weak, String other, String expected) {
        assertEquals(expected, weak.orElseGet(() -> other));
    }
    
    static Stream<Arguments> orElse_parameters() {
        return Stream.of(of(value, "", "value"), of(empty, "", ""));
    }
    
    
    @Test
    void orElseThrow() {
        assertEquals("value", value.orElseThrow(RuntimeException::new));
    }
    
    @Test
    void orElseThrow_ThrowsException() {
        assertThrows(RuntimeException.class, () -> empty.orElseThrow(RuntimeException::new));
    }
    
    
    @ParameterizedTest
    @MethodSource("stream_parameters")
    void stream(Weak<String> weak, int expected) {
        assertEquals(expected, weak.stream().toArray().length);
    }
    
    static Stream<Arguments> stream_parameters() {
        return Stream.of(of(value, 1), of(empty, 0));
    }
    
    
    @ParameterizedTest
    @MethodSource("weaks")
    void equals(boolean expected, Object other) {
        assertEquals(expected, value.equals(other));
    }
    
    @ParameterizedTest
    @MethodSource("weaks")
    void hashCode(boolean expected, Object other) {
        assertEquals(expected, value.hashCode() == other.hashCode());
    }
    
    static Stream<Arguments> weaks() {
        return Stream.of(
            of(true, value),
            of(true, new Weak<>("value")),
            of(false, empty),
            of(false, new Object())
        );
    }
    
}
