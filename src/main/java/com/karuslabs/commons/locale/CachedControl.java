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

import com.karuslabs.commons.annotation.*;

import java.util.*;
import java.util.ResourceBundle.Control;
import java.util.concurrent.*;
import javax.annotation.Nullable;

import static java.util.Collections.EMPTY_LIST;


public class CachedControl extends Control {
    
    public static final CachedControl NONE = new CachedControl() {
        
        @Override
        public ResourceBundle newBundle(@Ignored String name, @Ignored Locale locale, @Ignored String format, @Ignored ClassLoader loader, @Ignored boolean reload) {
            return CachedResourceBundle.NONE;
        }
        
    };
    
    
    private final ConcurrentMap<Locale, ResourceBundle> bundles;
    
    
    public CachedControl() {
        this(new ConcurrentHashMap<>());
    }
    
    public CachedControl(ConcurrentMap<Locale, ResourceBundle> bundles) {
        this.bundles = bundles;
    }
    
    
    @Override
    public @Nullable ResourceBundle newBundle(@Ignored String baseName, Locale locale, @Ignored String format, @Ignored ClassLoader loader, @Ignored boolean reload) {
        return bundles.get(locale);
    }
    
    @Override
    public @Immutable List<String> getFormats(@Ignored String bundleName) {
        return EMPTY_LIST;
    }
    
    
    public ConcurrentMap<Locale, ResourceBundle> getBundles() {
        return bundles;
    }
    
}
