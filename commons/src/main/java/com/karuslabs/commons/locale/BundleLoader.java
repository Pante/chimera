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

import com.fasterxml.jackson.databind.ObjectMapper;

import com.karuslabs.annotations.Ignored;
import com.karuslabs.commons.codec.decoders.LenientStringifier;

import java.io.*;
import java.util.*;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

import org.checkerframework.checker.nullness.qual.Nullable;


public class BundleLoader extends Control {
    
    private static final List<String> FORMATS = List.of("json", "properties", "yml", "yaml");
    private static final LenientStringifier STRINGIFIER = new LenientStringifier() {
        @Override
        public Map<String, Object> exceptional(IOException e) {
            return null;
        }
        
        @Override
        public ObjectMapper defaultMapper(String format) {
            return JSON;
        }
    };
    
    
    private Source[] sources;
    
    
    public BundleLoader(Source... sources) {
        this.sources = sources;
    }
    
    
    @Override
    public @Nullable ResourceBundle newBundle(String name, Locale locale, String format, @Ignored ClassLoader loader, @Ignored boolean reload) throws IOException {
        if (getFormats(name).contains(format)) {
            var bundle = toResourceName(toBundleName(name, locale), format);
            for (var source : sources) {
                try (var stream = source.stream(bundle)) {
                    if (stream != null) {
                        return load(stream, format);
                    }
                }
            }
        }
        
        return null;
    }
    
    protected @Nullable ResourceBundle load(InputStream stream, String format) {
        var map = STRINGIFIER.from(stream, format);
        return map != null ? new Bundle(map) : null;
    }
    
    
    @Override
    public List<String> getFormats(@Ignored String bundleName) {
        return FORMATS;
    }
    
}
