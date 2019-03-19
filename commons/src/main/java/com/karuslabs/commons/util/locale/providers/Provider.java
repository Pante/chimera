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

import java.util.Locale;

import org.bukkit.entity.Player;


/**
 * A provider from which locales that are mapped to a key can be retrieved.
 * 
 * @param <T> the type of the key
 */
@FunctionalInterface
public interface Provider<T> {
    
    /**
     * A {@code Provider} that returns the declared locale of a given player.
     */
    public static final Provider<Player> DETECTED = player -> Locales.of(player.getLocale());
    
    /**
     * A {@code Provider} that always returns the default locale defined by {@link Locale#getDefault()}.
     */
    public static final Provider NONE = key -> Locale.getDefault();
    
    
    /**
     * Returns the locale associated with the given key.
     * 
     * Implementations may differ on the default value to return if the provider
     * contains no mapping for the given key.
     * 
     * @param key the key
     * @return the locale to which the the given key is mapped
     */
    public Locale get(T key);
    
    /**
     * Returns the locale associated with the given key, or given default locale
     * if this provider contains no mapping for the key.
     * 
     * @param key the key
     * @param locale the default locale
     * @return the located associated with the given key, or the default locale if
     *         this provider contains no mapping for the key
     */
    public default Locale getOrDefault(T key, Locale locale) {
        var value = get(key);
        return value != null ? value : locale;
    }
    
    /**
     * Returns the default locale.
     * 
     * The default implementation returns the locale defined by {@link Locale#getDefault()}.
     * 
     * @return the default locale
     */
    public default Locale getDefault() {
        return Locale.getDefault();
    }
    
}
