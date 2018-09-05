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
package com.karuslabs.commons.locale.spi;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.karuslabs.commons.codec.decoders.LenientStringifier;
import com.karuslabs.commons.locale.*;

import java.io.IOException;
import java.util.*;
import java.util.ResourceBundle.Control;
import java.util.concurrent.ConcurrentHashMap;
import java.util.spi.ResourceBundleProvider;

import org.checkerframework.checker.nullness.qual.Nullable;


public class BundleProvider implements ResourceBundleProvider {

    static final Control CONTROL = Control.getControl(Control.FORMAT_DEFAULT);
    static final String[] FORMATS = new String[] {"json", "properties", "yml", "yaml"};
    static final Map<String, Source[]> SOURCES = new ConcurrentHashMap<>();
    
    
    static final LenientStringifier STRINGIFIER = new LenientStringifier() {
        @Override
        public Map<String, Object> exceptional(IOException e) {
            return null;
        }
        
        @Override
        public ObjectMapper defaultMapper(String format) {
            return JSON;
        }
    };
    
    
    public static void register(String name, Source... sources) {
        SOURCES.put(name, sources);
    }
    
    public static Source[] getSources(String name) {
        return SOURCES.get(name);
    }
    
    
    @Override
    public @Nullable ResourceBundle getBundle(String name, Locale locale) {
        var bundle = CONTROL.toBundleName(name, locale);
        for (var source : SOURCES.get(name)) {
            for (var format : FORMATS) {
                try (var stream = source.stream(bundle)) {
                    if (stream != null) {
                        var map = STRINGIFIER.from(stream, format);
                        return map != null ? new Bundle(map) : null;
                    }
                } catch (IOException e) {
                    return null;
                }
            }
        }
        
        return null;
    }
    
}
