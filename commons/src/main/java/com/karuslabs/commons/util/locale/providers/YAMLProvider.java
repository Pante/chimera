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
package com.karuslabs.commons.util.locale.providers;

import com.karuslabs.lingua.franca.Locales;

import java.io.*;
import java.util.*;

import org.bukkit.configuration.file.YamlConfiguration;


public class YAMLProvider implements Provider<UUID> {
    
    YamlConfiguration configuration;
    private File file;
    private Locale defaultLocale;
    
    
    public YAMLProvider(File file) {
        this(file, Locale.getDefault());
    }
    
    public YAMLProvider(File file, Locale defaultLocale) {
        this.configuration = YamlConfiguration.loadConfiguration(file);
        this.file = file;
        this.defaultLocale = defaultLocale;
    }
    
    
    @Override
    public Locale get(UUID key) {
        var locale = configuration.getString(key.toString());
        return locale != null ? Locales.of(locale) : defaultLocale; 
    }
    
    @Override
    public Locale getDefault() {
        return defaultLocale;
    }
    
    
    public YamlConfiguration configuration() {
        return configuration;
    }
    
    public void save() {
        try {
            configuration.save(file);
            
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
}
