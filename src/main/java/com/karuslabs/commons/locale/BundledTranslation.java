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

import com.karuslabs.commons.locale.bundle.Control;
import com.karuslabs.commons.locale.resources.Resource;

import java.util.*;
import javax.annotation.Nullable;


public class BundledTranslation extends Translation {
    
    private String name;
    private Control control;
    private ResourceBundle bundle;
    
    
    public BundledTranslation(String name, Resource... resources) {
        this(name, new Control(resources));
    }
    
    public BundledTranslation(String name, Control control) {
        this(Locale.getDefault(Locale.Category.FORMAT), name, control, ResourceBundle.getBundle(name, Locale.getDefault(Locale.Category.FORMAT), control));
    }
    
    public BundledTranslation(Locale locale,  String name, Control control, ResourceBundle bundle) {
        super(locale);
        this.name = name;
        this.control = control;
        this.bundle = bundle;
    }
    
    
    @Override
    public @Nullable String format(String key, Object... arguments) {
        String message = bundle.getString(key);
        return arguments.length == 0 ? message : apply(message, arguments);
    }
    
    @Override
    public Translation locale(Locale locale) {
        format.setLocale(locale);
        bundle = ResourceBundle.getBundle(name, locale, control);
        return this;
    }
    
    
    public String getBundleName() {
        return name;
    }

    
    @Override
    public BundledTranslation copy() {
        return new BundledTranslation(format.getLocale(), name, control, bundle);
    }
    
}
