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

import com.karuslabs.commons.locale.resources.EmbeddedResource;

import org.junit.jupiter.api.*;

import static java.util.Locale.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.*;


@TestInstance(PER_CLASS)
public class BundledTranslationTest {
    
    private BundledTranslation translation = spy(new BundledTranslation("Translation", new EmbeddedResource("locale")));
    
    
    @Test
    public void format() {
        assertEquals("value", translation.format("key"));
    }
    
    
    @Test
    public void locale() {
        translation.locale(JAPAN);
        
        assertEquals(JAPAN, translation.format.getLocale());
        assertEquals(JAPAN, translation.getBundle().getLocale());
    }
    
    
    @Test
    public void copy() {
        BundledTranslation other = translation.locale(PRC).copy();
        
        assertEquals(other.format.getLocale(), translation.format.getLocale());
        assertEquals(other.getBundleName(), translation.getBundleName());
        assertEquals(other.getControl(), translation.getControl());
        assertEquals(other.getBundle(), translation.getBundle());
    }
    
}
