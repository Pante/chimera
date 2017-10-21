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

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;


@TestInstance(PER_CLASS)
class LocalesTest {    
    
    @ParameterizedTest
    @CsvSource({"'', '', ''", "en, '', en", "'', US, US", "en, US, en_US"})
    void from(String language, String country, String expected) {
        Locale locale = new Locale(language, country);
        assertEquals(expected, Locales.from(locale));
    }
    
    @ParameterizedTest
    @CsvSource({"en_US, true", "US_en, false", "US, true", "en, true", "ZZ, false"})
    void get(String locale, boolean expected) {
        assertEquals(expected, Locales.get(locale) != null);
    }
    
    
    @ParameterizedTest
    @CsvSource({"en_US, true", "US_en, false", "US, true", "en, true", "ZZ, false"})
    void getOrDefault(String locale, boolean expected) {
        assertEquals(expected, Locales.getOrDefault(locale, (Locale) null) != null);
    }
    
    
    @ParameterizedTest
    @CsvSource({"en_US, true", "US_en, false", "US, true", "en, true", "ZZ, false"})
    void getOrDefault_Supplier(String locale, boolean expected) {
        assertEquals(expected, Locales.getOrDefault(locale, () -> null) != null);
    }
    
    
    @ParameterizedTest
    @CsvSource({"Ss, true", "ZP, false", "ZPZ, false"})
    void isCountry(String country, boolean expected) {
        assertEquals(expected, Locales.isCountry(country));
    } 
    
    
    @ParameterizedTest
    @CsvSource({"eN, true", "ZZ, false", "LSA, false"})
    void isLanguage(String language, boolean expected) {
        assertEquals(expected, Locales.isLanguage(language));
    }
    
}
