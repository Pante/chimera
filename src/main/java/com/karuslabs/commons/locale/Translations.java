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

import com.karuslabs.commons.locale.resources.Resource;

import java.util.*;
import javax.annotation.Nullable;


public class Translations {
    
    public static final Translations NONE = new Translations("") {
        
        @Override
        public Translation get(Locale locale) {
            return Translation.NONE;
        }
        
        @Override
        public @Nullable ResourceBundle getBundle(Locale locale) {
            return null;
        }
        
    };
    
    
    private String bundle;
    private Control control;
    
    
    public Translations(String bundle, Resource... resources) {
        this(bundle, new Control(resources));
    }
    
    public Translations(String bundle, Control control) {
        this.bundle = bundle;
        this.control = control;
    }
    
    
    public Translation get(Locale locale) {
        return new Translation(getBundle(locale));
    }
    
    public @Nullable ResourceBundle getBundle(Locale locale) {
        return ResourceBundle.getBundle(bundle, locale, control);
    }
    
    
    public String getBundleName() {
        return bundle;
    }
    
}
