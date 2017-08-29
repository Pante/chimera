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

import junitparams.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class LocalesTest {    
    
    @Test
    @Parameters({"en_US, true", "US_en, false", "US, false"})
    public void get(String locale, boolean expected) {
        assertEquals(expected, Locales.get(locale) != null);
    }
    
    
    @Test
    @Parameters({"en_US, true", "US_en, false", "US, false"})
    public void getOrDefault(String locale, boolean expected) {
        assertEquals(expected, Locales.getOrDefault(locale, Locale.getDefault()) != Locale.getDefault());
    }
    
    
    @Test
    @Parameters({"en_US, true, 0", "US_en, false, 1", "US, false, 1"})
    public void get_Supplier(String locale, boolean expected, int times) {
        Supplier<Locale> supplier = mock(Supplier.class);
        
        assertEquals(expected, Locales.get(locale, supplier) != null);
        verify(supplier, times(times)).get();
    }
    
    
    @Test
    @Parameters({"Ss, true", "ZP, false", "ZPZ, false"})
    public void isValidCountry(String country, boolean expected) {
        assertEquals(expected, Locales.isValidCountry(country));
    } 
    
    
    @Test
    @Parameters({"eN, true", "ZZ, false", "LSA, false"})
    public void isValidLanguage(String language, boolean expected) {
        assertEquals(expected, Locales.isValidLanguage(language));
    }
    
}
