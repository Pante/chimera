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

import java.io.IOException;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class BundleLoaderTest {
    
    static BundleLoader loader = new BundleLoader(Source.embedded("locales/standard"), Source.embedded("locales/others"));
    
    
    @Test
    void newBundle() throws IOException {
        assertNotNull(loader.newBundle("locales", Locale.FRENCH, "yml", getClass().getClassLoader(), true));
    }
    
    
    @Test
    void newBundle_invalid_format() throws IOException {
        assertNull(loader.newBundle("locales", Locale.FRENCH, "xml", getClass().getClassLoader(), true));
    }
    
    
    @Test
    void load() {
        var bundle = loader.load(getClass().getClassLoader().getResourceAsStream("locales/standard/locales.json"), "json");
        assertNotNull(bundle);
    }
    
    
    @Test
    void load_null() {
        var bundle = loader.load(getClass().getClassLoader().getResourceAsStream("locales/others/locales_fr.yml"), "json");
        assertNull(bundle);
    }
    
    
    @Test
    void getFormats() {
        assertEquals(List.of("json", "properties", "yml", "yaml"), loader.getFormats("anything"));
    }
    
}
