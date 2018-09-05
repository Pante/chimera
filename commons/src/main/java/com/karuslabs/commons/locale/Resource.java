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

import com.karuslabs.commons.locale.providers.Provider;
import com.karuslabs.commons.locale.spi.BundleProvider;

import java.text.MessageFormat;
import java.util.*;


public class Resource<T> extends MessageFormat {
    
    private String name;
    private Provider<T> provider;
    private ResourceBundle bundle;
    
    
    public Resource(String name, Source... sources) {
        this(name, (Provider<T>) Provider.NONE, sources);
    }
    
    public Resource(String name, Provider<T> provider, Source... sources) {
        this(name, Locale.getDefault(Locale.Category.FORMAT), provider, sources);
    }
    
    public Resource(String name, Locale locale, Provider<T> provider, Source... sources) {
        super("");
        this.name = name;
        this.provider = provider;
        this.bundle = ResourceBundle.getBundle(name, locale);
        BundleProvider.register(name, sources);
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
    
    
    public Resource locale(T key) {
        return locale(provider.get(key));
    }
    
    public Resource locale(T key, Locale locale) {
        return locale(provider.getOrDefault(key, locale));
    }
    
    public Resource locale(Locale locale) {
        setLocale(locale);
        return this;
    }
    
    @Override
    public void setLocale(Locale locale) {
        if (getLocale().equals(locale)) {
            this.bundle = ResourceBundle.getBundle(name, locale);
        }
        
        super.setLocale(locale);
    }
    
    public String getBaseName() {
        return name;
    }
    
    public Provider<T> getProvider() {
        return provider;
    }
    
    
    @Override
    public Resource clone() {
        return (Resource) super.clone();
    }
    
}
