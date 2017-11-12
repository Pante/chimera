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
package com.karuslabs.commons.locale.providers;

import com.karuslabs.commons.locale.Locales;
import com.karuslabs.commons.util.Get;

import java.util.Locale;
import javax.annotation.Nullable;

import org.bukkit.entity.Player;


/**
 * Provides the resolved locale for the specified {@code Player}.
 */
@FunctionalInterface
public interface Provider {
    
    /**
     * Provider which always return the default locale specified by {@link Locale#getDefault()}.
     */
    public static final Provider NONE = player -> Locale.getDefault();
    
    /**
     * Provider which returns the locale specified by {@link Player#getLocale()}, or {@code null} if 
     * the locale specified by {@link Player#getLocale()} is invalid.
     */
    public static final Provider DETECTED = player -> Locales.get(player.getLocale());
    
    
    /**
     * Returns the locale of the specified {@code Player}, or {@code null} if
     * this provider fails to resolve the locale of the specified {@code Player}.
     * 
     * @param player the player
     * @return the locale of the specified player, or null if this provider fails to resolve the locale
     */
    public @Nullable Locale get(Player player);
    
    /**
     * Returns the locale of the specified {@code Player}, or {@code locale} if this
     * provider fails to resolve the locale of the specified {@code Player}.
     * 
     * @param player the player
     * @param locale the default locale of the player
     * @return the resolved locale of the specified player, or locale if this provider fails to resolve the locale
     */
    public default Locale getOrDefault(Player player, Locale locale) {
       return Get.orDefault(get(player), locale);
    }
    
    /**
     * Returns the locale of the specified {@code Player}, or the detected locale specified by
     * {@link Player#getLocale()} if this provider fails to resolve the locale of the specified {@code Player}.
     * 
     * @param player the player
     * @return the locale of the specified player, or the detected locale if this provider fails to resolve the locale
     */
    public default Locale getOrDetected(Player player) {
        Locale locale = get(player);
        if (locale != null) {
            return locale;
            
        } else {
            return Locales.getOrDefault(player.getLocale(), Locale.getDefault());
        }
    }
    
}
