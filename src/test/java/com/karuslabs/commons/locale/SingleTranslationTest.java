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
package com.karuslabs.commons.locale;

import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static java.util.Locale.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.junit.jupiter.params.provider.Arguments.of;


@TestInstance(PER_CLASS)
public class SingleTranslationTest {
    
    private SingleTranslation translation;
    
    
    public SingleTranslationTest() {
        translation = new SingleTranslation(new ConcurrentHashMap<>());
        translation.getMessages().put("key", "value {0}");
    }
    
    
    @ParameterizedTest
    @MethodSource("format_parameters")
    public void format(String key, Object[] arguments, String expected) {
        assertEquals(expected, translation.format(key, arguments));
    }
    
    static Stream<Arguments> format_parameters() {
        return Stream.of(of("key", new Object[] {1}, "value 1"), of("null", new Object[] {1}, null), of("key", new Object[] {}, "value {0}"));
    }
    
    
    @Test
    public void copy() {
        translation.format.setLocale(PRC);
        SingleTranslation other = translation.copy();
        
        assertEquals(translation.format.getLocale(), other.format.getLocale());
        assertEquals(translation.getMessages(), other.getMessages());
    }
    
}
