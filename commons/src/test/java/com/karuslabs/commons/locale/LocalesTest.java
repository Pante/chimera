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

package com.karuslabs.commons.locale;

import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;


@ExtendWith(MockitoExtension.class)
class LocalesTest {
    
    private static final String TAG = "en_UD";
    private static final Locale LOCALE = new Locale("en", "UD");
    
    
    @ParameterizedTest
    @MethodSource({"locales_provider"})
    void of_locale(String tag, Locale locale) {
        var preloaded = Locales.of(LOCALE);
        
        assertEquals(tag, Locales.of(locale));
    }
    
    
    @ParameterizedTest
    @MethodSource({"locales_provider"}) 
    void of_tag(String tag, Locale locale) {
        var preloaded = Locales.of(TAG);
        
        assertEquals(locale, Locales.of(tag));
    }
    
    
    static Stream<Arguments> locales_provider() {
        return Stream.of(of("en_UD", LOCALE));
    }
    
        
    @ParameterizedTest
    @CsvSource({"ENGLISH, false", "EN, true", "en, true"})
    void isISOLanguage(String language, boolean expected) {
        assertEquals(expected, Locales.isISOLanguage(language));
    }
    
        
    @ParameterizedTest
    @CsvSource({"SINGAPORE, false", "SG, true", "sg, true"})
    void isISOCountry(String country, boolean expected) {
        assertEquals(expected, Locales.isISOCountry(country));
    }
    
}
