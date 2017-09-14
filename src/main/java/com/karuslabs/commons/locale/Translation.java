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

import com.karuslabs.commons.util.Prototype;

import java.text.MessageFormat;
import java.util.Locale;
import javax.annotation.Nullable;


public abstract class Translation implements Prototype<Translation> {
    
    public static final Translation NONE = new Translation() {
        
        @Override
        public String format(String key, Object... arguments) {
            return key;
        }

        @Override
        public Translation copy() {
            return Translation.NONE;
        }
        
    };
    
    
    protected MessageFormat format;
    
    
    public Translation() {
        this(Locale.getDefault());
    }
    
    public Translation(Locale locale) {
        format = new MessageFormat("");
    }
    
    
    public abstract @Nullable String format(String key, Object... arguments);
    
    public Translation locale(Locale locale) {
        format.setLocale(locale);
        return this;
    }
    
}
