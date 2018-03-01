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

import com.karuslabs.commons.annotation.Ignored;
import com.karuslabs.commons.locale.providers.Provider;

import java.text.MessageFormat;
import java.util.*;
import java.util.ResourceBundle.Control;
import javax.annotation.Nonnull;

import org.bukkit.entity.Player;


/**
 * A concrete subclass of {@code Translation} which provides facilities for formatting messages.
 */
public class MessageTranslation extends Translation {
    
    /**
     * An empty {@code MessageTranslation} which returns an empty {@code ResourceBundle} when {@link #get(Locale)} is invoked and key when
     * {@link #format(String, Object...)} is invoked.
     */
    public static final MessageTranslation NONE = new MessageTranslation("", ExternalControl.NONE, Provider.NONE) {
        
        @Override
        public @Nonnull ResourceBundle get(@Ignored Locale locale) {
            return CachedResourceBundle.NONE;
        }
        
        @Override
        public String format(String key, @Ignored Object... arguments) {
            return key;
        }
        
        @Override
        public MessageTranslation locale(@Ignored Locale locale) {
            return this;
        }
        
    };
    
    
    /**
     * Represents the {@code MessageFormat} used to format messages.
     */
    protected MessageFormat format;
    /**
     * Represents the cached {@code ResourceBundle} which was last used.
     */
    protected ResourceBundle bundle;
    
    
    /**
     * Constructs a {@code MessageTranslation} with the specified bundle name, {@code Control} and {@code Provider}.
     * 
     * @param bundle the base bundle name
     * @param control the control which provides the ResourceBundles
     * @param provider the provider which provides the locales of the players
     */
    public MessageTranslation(String bundle, Control control, Provider provider) {
        super(bundle, control, provider);
        format = new MessageFormat("");
    }
    
    
    /**
     * Returns the message associated with the specified key formatted using the specified arguments.
     * 
     * @param key the key
     * @param arguments the arguments
     * @return the formatted message
     */
    public String format(String key, Object... arguments) {
        if (bundle == null) {
            bundle = get(format.getLocale());
        }
        
        format.applyPattern(bundle.getString(key));
        return format.format(arguments);
    }
    
    /**
     * Sets the locale of this {@code MessageTranslation} to the locale of the specified {@code Player}.
     * 
     * @param player the player
     * @return this
     */
    public MessageTranslation locale(Player player) {
        return locale(provider.get(player));
    }
    
    /**
     * Sets the locale of this {@code MessageTranslation} to the locale of the specified {@code Player},
     * or the specified {@code locale} if this {@code MessageTranslation} is unable to resolve the locale
     * of the specified {@code Player}.
     * 
     * @param player the player
     * @param locale the default locale
     * @return this
     */
    public MessageTranslation localeOrDefault(Player player, Locale locale) {
        return locale(provider.getOrDefault(player, locale));
    }
    
    /**
     * Sets the locale of this {@code MessageTranslation} to the locale of the specified {@code Player},
     * or the detected locale if this {@code MessageTranslation} is unable to resolve the locale of the
     * specified {@code Player}.
     * 
     * @param player the player
     * @return this
     */
    public MessageTranslation localeOrDetected(Player player) {
        return locale(provider.getOrDetected(player));
    }
    
    /**
     * Sets the locale of this {@code MessageTranslation} to the specified locale.
     * 
     * @param locale the locale
     * @return this
     */
    public MessageTranslation locale(Locale locale) {
        format.setLocale(locale);
        bundle = null;
        return this;
    }
    
    
    /**
     * Creates a {@code MessageTranslation} builder.
     * 
     * @return the Builder
     */
    public static Builder<MessageTranslation> builder() {
        return new Builder<>(MessageTranslation::new, "", Provider.DETECTED);
    }
    
}
