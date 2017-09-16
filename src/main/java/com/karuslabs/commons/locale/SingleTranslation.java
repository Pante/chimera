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

import java.util.Locale;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nullable;


public class SingleTranslation extends Translation {
    
    private ConcurrentMap<String, String> messages;

    
    public SingleTranslation(ConcurrentMap<String, String> cache) {
        this(Locale.getDefault(Locale.Category.FORMAT), cache);
    }
    
    public SingleTranslation(Locale locale, ConcurrentMap<String, String> cache) {
        super(locale);
        this.messages = cache;
    }
    
    
    @Override
    public @Nullable String format(String key, Object... arguments) {
        String message = messages.get(key);
        
        if (message != null && arguments.length != 0) {
            return apply(message, arguments);
            
        } else {
            return message;
        }
    }
    
    
    public ConcurrentMap<String, String> getMessages() {
        return messages;
    }

    
    @Override
    public SingleTranslation copy() {
        return new SingleTranslation(format.getLocale(), messages);
    }
    
}
