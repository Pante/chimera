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

import com.google.common.cache.*;
import com.google.common.collect.*;

import com.karuslabs.annotations.Static;

import java.util.*;

import static java.util.Locale.ENGLISH;
import static java.util.concurrent.TimeUnit.MINUTES;


public @Static class Locales {
    
    private static final BiMap<String, Locale> LOCALES;
    
    private static final Cache<String, Locale> STRING_CACHE = CacheBuilder.newBuilder().expireAfterAccess(5, MINUTES).build();
    private static final Cache<Locale, String> LOCALE_CACHE = CacheBuilder.newBuilder().expireAfterAccess(5, MINUTES).build();
    
    private static final Set<String> LANGUAGES = Set.of(Locale.getISOLanguages());
    private static final Set<String> COUNTRIES = Set.of(Locale.getISOCountries());
    
    
    static {
        var locales = Locale.getAvailableLocales();
        LOCALES = HashBiMap.create(locales.length);
        
        for (var locale : locales) {
            LOCALES.put(locale.toLanguageTag().replace('-', '_'), locale);
        }
    }
    
    
    public static String of(Locale locale) {
        var tag = LOCALES.inverse().get(locale);
        tag = tag == null ? LOCALE_CACHE.getIfPresent(locale) : tag;
        if (tag == null) {
            tag = locale.toLanguageTag().replace('-', '_'); 
            LOCALE_CACHE.put(locale, tag);
        }
        
        return tag;
    }
    
    public static Locale of(String tag) {
        var locale = LOCALES.get(tag);
        locale = locale == null ? STRING_CACHE.getIfPresent(tag) : locale;
        if (locale == null) {
            locale = Locale.forLanguageTag(tag.replace('_', '-'));
            STRING_CACHE.put(tag, locale);
        }
        
        return locale;
    }
    
    
    public static boolean isISOLanguage(String language) {
        return language.length() == 2 && LANGUAGES.contains(language.toLowerCase(ENGLISH));
    }
    
    public static boolean isISOCountry(String country) {
        return country.length() == 2 && COUNTRIES.contains(country.toUpperCase(ENGLISH));
    }
    
}
