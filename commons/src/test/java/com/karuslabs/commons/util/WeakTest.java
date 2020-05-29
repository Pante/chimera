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

package com.karuslabs.commons.util;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


class WeakTest {
    
    static final String VALUE = "test";
    static final Weak<String> WEAK = Weak.of(VALUE);
    
    
    @ParameterizedTest
    @MethodSource("weaks")
    void filter(Weak<String> reference, boolean empty) {
        assertEquals(empty, reference.filter(value -> true).equals(Weak.empty()));
    }
    
    
    @ParameterizedTest
    @MethodSource("weaks")
    void flatMap(Weak<String> reference, boolean empty) {
        assertEquals(empty, reference.flatMap(value -> Weak.of(VALUE)).equals(Weak.empty()));
    }
    
    
    @ParameterizedTest
    @MethodSource("weaks")
    void map(Weak<String> reference, boolean empty) {
        assertEquals(empty, reference.map(value -> VALUE).equals(Weak.empty()));
    }
    
    
    @ParameterizedTest
    @MethodSource("weaks")
    void orElse(Weak<String> reference, boolean empty) {
        assertEquals(empty, reference.orElse(Weak::empty).equals(Weak.empty()));
    }
    
    @ParameterizedTest
    @MethodSource("weaks")
    void or_value(Weak<String> reference, boolean expected) {
        assertEquals(expected, reference.or("OTHER").equals("OTHER"));
    }
    
    
    @ParameterizedTest
    @MethodSource("weaks")
    void or_supplier(Weak<String> reference, boolean expected) {
        assertEquals(expected, reference.or(() -> "OTHER").equals("OTHER"));
    }
    
    
    @Test
    void orThrow() {
        assertEquals(VALUE, WEAK.orThrow());
    }
    
    
    @Test
    void orThrow_exception() {
        assertEquals("Value was reclaimed", assertThrows(NoSuchElementException.class, () -> Weak.empty().orThrow()).getMessage());
    }
    
    
    @Test
    void orThrow_supplier() {
        assertEquals(VALUE, WEAK.orThrow(RuntimeException::new));
    }
    
    
    @Test
    void orThrow_supplier_exception() {
        assertThrows(RuntimeException.class , () -> Weak.empty().orThrow(RuntimeException::new));
    }
    
    
    @ParameterizedTest
    @MethodSource("weaks")
    void ifPresent(Weak<String> reference, boolean unconsumed) {
        Consumer<String> consumer = mock(Consumer.class);
        reference.ifPresent(consumer);
        
        verify(consumer, times(unconsumed ? 0 : 1)).accept(VALUE);
    }
    
    
    @ParameterizedTest
    @MethodSource("weaks")
    void ifPresent_otherwise(Weak<String> reference, boolean unconsumed) {
        Consumer<String> consumer = mock(Consumer.class);
        Runnable otherwise = mock(Runnable.class);
        
        reference.ifPresent(consumer, otherwise);
        
        verify(consumer, times(unconsumed ? 0 : 1)).accept(VALUE);
        verify(otherwise, times(unconsumed ? 1: 0)).run();
    }
    
    
    @ParameterizedTest
    @MethodSource("weaks")
    void isPresent(Weak<String> reference, boolean empty) {
        assertEquals(empty, !reference.isPresent());
    }
    
    
    @ParameterizedTest
    @MethodSource("weaks")
    void stream(Weak<String> reference, boolean empty) {
        assertEquals(empty, reference.stream().count() == 0);
    }
    
    
    @Test
    void equals() {
        assertFalse(WEAK.equals("string"));
    }
    
    
    @Test
    void hash() {
        assertEquals(VALUE.hashCode(), Weak.of("test").hashCode());
    }
    
    
    static Stream<Arguments> weaks() {
        return Stream.of(of(WEAK, false), of(Weak.empty(), true));
    }   
    
}
