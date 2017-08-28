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

import java.text.MessageFormat;
import java.util.*;


public class Translation {
    
    private ResourceBundle bundle;
    private Text text;

    
    public Translation(ResourceBundle bundle) {
        this.bundle = bundle;
        this.text = new Text("", bundle.getLocale());
    }

    
    public Text get(String key) {
        text.set(bundle.getString(key));
        return text;
    }
    
    public Text getImmutable(String key) {
        return new Text(bundle.getString(key), bundle.getLocale());
    }
    
    
    public static class Text {

        private String text;
        private MessageFormat format;

        
        public Text(String text) {
            this.text = text;
            format = new MessageFormat("");
        }

        public Text(String text, Locale locale) {
            this(text);
            format.setLocale(locale);
        }

        
        public String text() {
            return text;
        }

        public String format(Object... arguments) {
            format.applyPattern(text);
            return format.format(arguments);
        }

        
        protected void set(String text) {
            this.text = text;
        }

    }

}
