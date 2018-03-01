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

import com.karuslabs.commons.locale.providers.Provider;

import java.util.*;
import java.util.ResourceBundle.Control;

import org.bukkit.entity.Player;


/**
 * Represents a translation which provides variants of a default {@code ResourceBundle} for different locales.
 * 
 * This class will attempt to retrieve the most relevant {@code ResourceBundle} if it is unable to resolve a locale.
 */
public class Translation {
    
    private String bundle;
    private Control control;
    protected Provider provider;
    
    
    /**
     * Constructs a {@code Translation} with the specified name, {@code Control} and {@code Provider}.
     * 
     * @param bundle the bundle name
     * @param control the control which provides the ResourceBundles
     * @param provider the provider which provides the locales of the players
     */
    public Translation(String bundle, Control control, Provider provider) {
        this.bundle = bundle;
        this.control = control;
        this.provider = provider;
    }
         
    
    /**
     * Returns the {@code ResourceBundle} for the locale of the specified {@code Player}.
     * 
     * @param player the player
     * @return the ResourceBundle for the locale of the specified Player if 
     */
    public ResourceBundle get(Player player) {
        return get(provider.get(player));
    }
    
    /**
     * Returns the {@code ResourceBundle} for the locale of the specified {@code Player}, or the specified {@code locale}
     * if the locale of the specified {@code Player} is unable to be resolved.
     * 
     * @param player the player
     * @param locale the default locale
     * @return the ResourceBundle for the locale of the specified Player if able to be resolved; else the specified locale
     */
    public ResourceBundle getOrDefault(Player player, Locale locale) {
        return get(provider.getOrDefault(player, locale));
    }
    
    /**
     * Returns the {@code ResourceBundle} for the locale of the specified {@code Player}, or the detected locale
     * if the locale of the specified {@code Player} is unable to be resolved.
     * 
     * @param player the player
     * @return the ResourceBundle for the locale of the specified Player if able to be resolved; else the detected locale
     */
    public ResourceBundle getOrDetected(Player player) {
        return get(provider.getOrDetected(player));
    }
    
    /**
     * Returns the {@code ResourceBundle} for the specified {@code locale}.
     * 
     * @param locale the locale
     * @return the ResourceBundle for the specified locale;
     */
    public ResourceBundle get(Locale locale) {
        return ResourceBundle.getBundle(bundle, locale, control);
    }
    
    
    /**
     * Returns the name of the base {@code ResourceBundle}.
     * 
     * @return the bundle name
     */
    public String getBundleName() {
        return bundle;
    }
    
    /**
     * Returns the {@code Control} which retrieves the {@code ResourceBundle}s.
     * 
     * @return the Control
     */
    public Control getControl() {
        return control;
    }
    
    /**
     * Returns the {@code Provider} which provides the locales of {@code Player}s.
     * 
     * @return the Provider
     */
    public Provider getProvider() {
        return provider;
    }
    
    
    /**
     * Creates a {@code Translation} builder.
     * 
     * @return the Builder
     */
    public static Builder<? extends Translation> builder() {
        return new Builder<>(Translation::new, "", Provider.DETECTED);
    }
    
}
