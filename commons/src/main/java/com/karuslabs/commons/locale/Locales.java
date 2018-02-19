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

import com.google.common.cache.Cache;

import com.karuslabs.commons.annotation.Static;
import com.karuslabs.commons.util.Get;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Supplier;
import javax.annotation.Nullable;

import static com.google.common.cache.CacheBuilder.newBuilder;
import static java.util.Arrays.*;
import static java.util.Locale.*;
import static java.util.concurrent.TimeUnit.MINUTES;


@Static 
public class Locales {
    
    private static final Map<String, Locale> CONSTANTS = new HashMap<>();
    private static final Cache<String, Locale> CACHE = newBuilder().expireAfterAccess(5, MINUTES).build();
    
    private static final Set<String> COUNTRIES = new HashSet<>(asList(getISOCountries()));
    private static final Set<String> LANGUAGES = new HashSet<>(asList(getISOLanguages()));
    
    
    static {
        try {
            Field[] fields = Locale.class.getFields();
            for (Field field : fields) {
                if (field.getType() == Locale.class) {
                    Locale locale = (Locale) field.get(null);
                    CONSTANTS.put(from(locale), locale);
                }
            }
            
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to load constant Locales", e);
        }
    }

    
    public static String from(Locale locale) {
        String language = locale.getLanguage();
        String country = locale.getCountry();
        
        if (!language.isEmpty() && !country.isEmpty()) {
            return language + "_" + country;
            
        } else if (!language.isEmpty()) {
            return language;
            
        } else if (!country.isEmpty()) {
            return country;
            
        } else {
            return "";
        }
    }
    
    
    public static @Nullable Locale get(String locale) {
        Locale retrieved;
        if ((retrieved = CONSTANTS.get(locale)) != null) {
            return retrieved;
            
        } else if ((retrieved = CACHE.getIfPresent(locale)) != null) {
            return retrieved;
            
        } else {
            return from(locale);
        }
    }
    
    static @Nullable Locale from(String locale) {
        String[] parts;
        if (isCountry(locale)) {
            return new Locale("", locale);
            
        } else if (isLanguage(locale)) {
            return new Locale(locale);
            
        } else if ((parts = locale.split("_")).length == 2 && isLanguage(parts[0]) && isCountry(parts[1])) {
            Locale created = new Locale(parts[0], parts[1]);
            CACHE.put(locale, created);
            return created;
            
        } else {
            return null;
        }
    }
    
    
    public static Locale getOrDefault(String locale) {
        return Get.orDefault(get(locale), getDefault());
    }
    
    public static Locale getOrDefault(String locale, Locale value) {
        return Get.orDefault(get(locale), value);
    }
    
    public static Locale getOrDefault(String locale, Supplier<Locale> supplier) {
        return Get.orDefault(get(locale), supplier);
    }
    
        
    public static boolean isCountry(String country) {
        return country.length() == 2 && COUNTRIES.contains(country.toUpperCase(ENGLISH));
    }
    
    public static boolean isLanguage(String language) {
        return language.length() == 2 && LANGUAGES.contains(language.toLowerCase(ENGLISH));
    }
    
}
