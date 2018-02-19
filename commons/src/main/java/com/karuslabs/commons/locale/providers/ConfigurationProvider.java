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


public class ConfigurationProvider implements Provider {
    
    private BackedConfiguration config;
    
    
    public ConfigurationProvider(BackedConfiguration config) {
        this.config = config;
    }
    
    
    @Override
    public @Nullable Locale get(Player player) {
        String id = player.getUniqueId().toString();
        String locale = config.getString(id);
        return Locales.get(locale);
    }
    
    public void set(Player player, Locale locale) {
        String id = player.getUniqueId().toString();
        String formatted = locale.getLanguage() + "_" + locale.getCountry();
        if (formatted.startsWith("_") || formatted.endsWith("_")) {
            formatted = formatted.replace("_", "");
        }
        
        config.set(id, formatted);
    }
        
    
    public void save() {
        config.save();
    }
    
}
