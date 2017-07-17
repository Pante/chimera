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
package com.karuslabs.commons.language.bundle;

import com.karuslabs.commons.configuration.Configurations;

import java.util.*;


public class YamlResourceBundle extends ResourceBundle {
    
    private Map<String, Object> entries;
    
    
    public YamlResourceBundle(Map<String, Object> entries) {
        this.entries = entries;
    }
    
    
    @Override
    public Enumeration<String> getKeys() {
        return Collections.enumeration(entries.keySet());
    }
    
    @Override
    protected Object handleGetObject(String key) {
        return entries.get(key);
    }
    
    
    public static class Control extends ResourceBundle.Control {
        
        public static final Control INSTANCE = new Control();
        
        
        private static final List<String> FORMATS = Arrays.asList("yml", "yaml");
        
        protected Control() {}
        
        
        @Override
        public List<String> getFormats(String baseName) {
            return FORMATS;
        }
        
        
        @Override
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) {
            if (FORMATS.contains(format)) {
                String resource = toResourceName(toBundleName(baseName, locale), format);
                return new YamlResourceBundle(Configurations.flatten(Configurations.from(loader.getResourceAsStream(resource))));
                
            } else {
                return null;
            }
        }
        
    }
    
}
