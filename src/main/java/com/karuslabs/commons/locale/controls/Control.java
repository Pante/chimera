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
package com.karuslabs.commons.locale.controls;

import com.karuslabs.commons.locale.resources.Resource;

import java.io.InputStream;
import java.util.*;
import javax.annotation.Nullable;


public abstract class Control extends ResourceBundle.Control {
    
    private List<Resource> resources;
    
    
    public Control(List<Resource> resources) {
        this.resources = resources;
    }
    
    
    @Override
    public @Nullable ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) {
        if (getFormats(baseName).contains(format)) {
            String bundleName = toBundleName(baseName, locale);
            String path = toResourceName(bundleName, format);
            for (Resource resource : resources) {
                if (resource.exists(path)) {
                    return load(resource.load(path));
                }
            }
        }
            
        return null;
    }
    
    protected abstract ResourceBundle load(InputStream stream);
    
    
    public List<Resource> getResources() {
        return resources;
    }

}
