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


/**
 * A {@code Provider} subclass in which locales that are mapped to UUIDs and stored
 * in a YAML file can be retrieved.
 */
public class YAMLProvider implements Provider<UUID> {
    
    YamlConfiguration configuration;
    private File file;
    private Locale defaultLocale;
    
    
    /**
     * Creates a {@code YAMLProvider} from the given YAML file and a default locale 
     * as defined by {@link Locale#getDefault()}.
     * 
     * @param file the YAML file
     */
    public YAMLProvider(File file) {
        this(file, Locale.getDefault());
    }
    
    /**
     * Creates a {@code YAMLProvider} from the given YAML file and default locale.
     * 
     * @param file the YAML file
     * @param defaultLocale the default locale to return if this provider contains
     *                      no mapping for a given UUID
     */
    public YAMLProvider(File file, Locale defaultLocale) {
        this.configuration = YamlConfiguration.loadConfiguration(file);
        this.file = file;
        this.defaultLocale = defaultLocale;
    }
    
    
    /**
     * Returns the locale associated with the given UUID, or {@code locale} if this 
     * provider contains no mapping for the UUID.
     * 
     * @param key the UUID
     * @return the locale to which the the given UUID is mapped, or {@code locale}
     *         if this provider contains no mapping for the UUID
     */
    @Override
    public Locale get(UUID key) {
        var locale = configuration.getString(key.toString());
        return locale != null ? Locales.of(locale) : defaultLocale; 
    }
    
    /**
     * Returns the default locale.
     * 
     * @return the default locale
     */
    @Override
    public Locale getDefault() {
        return defaultLocale;
    }
    
    
    /**
     * Returns the underlying YAML configuration.
     * 
     * @return the YAML configuration
     */
    public YamlConfiguration configuration() {
        return configuration;
    }
    
    /**
     * Saves any changes made to the underlying YAML configuration.
     * 
     * @throws UncheckedIOException if changes to the underlying YAML configuration
     *                              failed
     */
    public void save() {
        try {
            configuration.save(file);
            
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
}
