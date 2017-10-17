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

import java.util.Locale;
import java.util.function.Supplier;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.*;


@TestInstance(PER_CLASS)
class LocalesTest {    
    
    @ParameterizedTest
    @CsvSource({"en_US, true", "US_en, false", "US, false"})
    void get(String locale, boolean expected) {
        assertEquals(expected, Locales.get(locale) != null);
    }
    
    
    @ParameterizedTest
    @CsvSource({"en_US, true", "US_en, false", "US, false"})
    void getOrDefault(String locale, boolean expected) {
        assertEquals(expected, Locales.getOrDefault(locale, Locale.getDefault()) != Locale.getDefault());
    }
    
    
    @ParameterizedTest
    @CsvSource({"en_US, true, 0", "US_en, false, 1", "US, false, 1"})
    void getOrDefault_Supplier(String locale, boolean expected, int times) {
        Supplier<Locale> supplier = mock(Supplier.class);
        
        assertEquals(expected, Locales.getOrDefault(locale, supplier) != null);
        verify(supplier, times(times)).get();
    }
    
    
    @ParameterizedTest
    @CsvSource({"Ss, true", "ZP, false", "ZPZ, false"})
    void isValidCountry(String country, boolean expected) {
        assertEquals(expected, Locales.isValidCountry(country));
    } 
    
    
    @ParameterizedTest
    @CsvSource({"eN, true", "ZZ, false", "LSA, false"})
    void isValidLanguage(String language, boolean expected) {
        assertEquals(expected, Locales.isValidLanguage(language));
    }
    
}
