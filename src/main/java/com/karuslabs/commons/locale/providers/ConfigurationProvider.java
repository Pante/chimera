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

import com.karuslabs.commons.configuration.BackedConfiguration;
import com.karuslabs.commons.locale.Locales;

import java.util.Locale;
import javax.annotation.Nullable;

import org.bukkit.entity.Player;


/**
 * A {@code Provider} implementation backed by a configuration file.
 * 
 * Locales of {@code Player}s are stored in the configuration file by
 * mapping the UUID of the {@code Player} to the specified locale in the format, 
 * {@code [Player UUID]: "[ISO 3166 alpha-2 langauge]_[ISO 639 alpha-2 country]"}.
 * 
 * Changes are not automatically saved to disk and must be manually triggered via {@link #save()}.
 */
public class ConfigurationProvider implements Provider {
    
    private BackedConfiguration config;
    
    
    /**
     * Constructs a new {@code ConfigurationProvider} backed by the specified {@code BackedConfiguration}.
     * 
     * @param config the configuration
     */
    public ConfigurationProvider(BackedConfiguration config) {
        this.config = config;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable Locale get(Player player) {
        String id = player.getUniqueId().toString();
        String locale = config.getString(id);
        return Locales.get(locale);
    }
    
    /**
     * 
     * 
     * @param player
     * @param locale 
     */
    public void set(Player player, Locale locale) {
        String id = player.getUniqueId().toString();
        config.set(id, locale.getLanguage() + "_" + locale.getCountry());
    }
    
    
    /**
     * Saves the changes made to disk.
     */
    public void save() {
        config.save();
    }
    
}
