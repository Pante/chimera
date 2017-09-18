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

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class TranslationTest {
    
    private Translation translation;
    
    
    public TranslationTest() {
        translation = new Translation() {
            @Override
            protected String value(String key) {
                return key;
            }

            @Override
            public Translation copy() {
                return null;
            }
        };
    }
    
    
    @Test
    public void format() {
        assertEquals("applied key", translation.format("applied {0}", "key"));
    }
    
    
    @Test
    public void locale() {
        translation.format.setLocale(Locale.ENGLISH);
        translation.locale(Locale.ITALY);
        
        assertEquals(Locale.ITALY, translation.format.getLocale());
    }
    
    
    @Test
    public void none_Locale() {
        Locale before = Translation.NONE.format.getLocale();
        Translation.NONE.locale(Locale.ROOT);
        assertEquals(before, Translation.NONE.format.getLocale());
    }
    
    
    @Test
    public void none_copy() {
        assertEquals(Translation.NONE, Translation.NONE.copy());
    }
    
}
