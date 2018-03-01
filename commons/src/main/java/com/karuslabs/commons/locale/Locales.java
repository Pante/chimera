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


/**
 * This class consists exclusively of static methods which operate on or return {@code Locale}s.
 * <p>
 * The standard string representation for {@code Locale}s is {@code [ISO 3166 alpha-2 language]_[ISO 639 alpha-2 country]} i.e. {@code EN_UK}, with the
 * underscore omitted in the absence of either the language or country.
 */
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

    
    /**
     * Returns a {@code String} representing the specified {@code Loclae}.
     * 
     * @param locale the locale
     * @return a standard String representation of the specified Locale
     */
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
    
    
    /**
     * Creates a {@code Locale} from the specified standard {@code String} representation of a {@code Locale},
     * or {@code null} if the specified standard {@code String} representation is malformed.
     * 
     * @param locale the standard String representation of a Locale
     * @return a Locale if the specified String representation is properly formatted; else null
     */
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
    
    /**
     * Creates a {@code Locale} from the specified standard {@code String} representation of a {@code Locale},
     * or the System default specified by {@link Locale#getDefault()} if the specified standard {@code String} representation is malformed.
     * 
     * @param locale the standard String representation of a Locale
     * @return a Locale if the specified String representation is properly formatted; else the specified value
     */
    public static Locale getOrDefault(String locale) {
        return Get.orDefault(get(locale), getDefault());
    }
    
    /**
     * Creates a {@code Locale} from the specified standard {@code String} representation of a {@code Locale},
     * or {@code value} if the specified standard {@code String} representation is malformed.
     * 
     * @param locale the standard String representation of a Locale
     * @param value the default value
     * @return a Locale if the specified String representation is properly formatted; else the specified value
     */
    public static Locale getOrDefault(String locale, Locale value) {
        return Get.orDefault(get(locale), value);
    }
    
    /**
     * Creates a {@code Locale} from the specified standard {@code String} representation of a {@code Locale},
     * or the {@code Locale} produced by the specified {@code Supplier} if the specified standard {@code String} 
     * representation is malformed.
     * 
     * @param locale the standard String representation of a Locale
     * @param supplier the supplying function which produces a value
     * @return a Locale if the specified String representation is properly formatted; else the value produced by the specified Supplier
     */
    public static Locale getOrDefault(String locale, Supplier<Locale> supplier) {
        return Get.orDefault(get(locale), supplier);
    }
    
        
    /**
     * Returns {@code true} if the specified {@code country} is an ISO 639 alpha-2 country i.e. {@code UK}; else {@code false}.
     * 
     * @param country the country
     * @return true if the specified country is an ISO 639 alpha-2 country country; else false
     */
    public static boolean isCountry(String country) {
        return country.length() == 2 && COUNTRIES.contains(country.toUpperCase(ENGLISH));
    }
    
    /**
     * Returns {@code true} if the specified {@code language} is an ISO 3166 alpha-2 language i.e. {@code EN}; else {@code false}.
     * 
     * @param language the language
     * @return true if the specified country is an ISO 3166 alpha-2 language; else false
     */
    public static boolean isLanguage(String language) {
        return language.length() == 2 && LANGUAGES.contains(language.toLowerCase(ENGLISH));
    }
    
}
