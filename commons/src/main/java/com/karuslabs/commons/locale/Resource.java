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

import java.text.MessageFormat;
import java.util.*;


public class Resource extends MessageFormat {
    
    private BundleLoader loader;
    private String name;
    private ResourceBundle bundle;
    
    
    public Resource(BundleLoader loader, String name) {
        this(loader, name, Locale.getDefault(Locale.Category.FORMAT));
    }
    
    public Resource(BundleLoader loader, String name, Locale locale) {
        super("");
        this.loader = loader;
        this.name = name;
        this.bundle = ResourceBundle.getBundle(name, locale, loader);
    }
    
        
    public String get(String key) {
        return bundle.getString(key);
    }
    
    public String get(String key, Object... arguments) {
        return format(bundle.getString(key), arguments);
    }
    
    
    public String at(String key, int index) {
        return bundle.getStringArray(key)[index];
    }
    
    public String at(String key, int index, Object... arguments) {
        return format(bundle.getStringArray(key)[index], arguments);
    }
    
    
    public String[] all(String key) {
        return bundle.getStringArray(key);
    }
    
    
    public Resource locale(Locale locale) {
        setLocale(locale);
        return this;
    }
    
    @Override
    public void setLocale(Locale locale) {
        if (getLocale().equals(locale)) {
            this.bundle = ResourceBundle.getBundle(name, locale, loader);
        }
        
        super.setLocale(locale);
    }
    
    
    public String getBasename() {
        return name;
    }
    
    public BundleLoader getBundleLoader() {
        return loader;
    }
    
    
    @Override
    public Resource clone() {
        return (Resource) super.clone();
    }
    
}
