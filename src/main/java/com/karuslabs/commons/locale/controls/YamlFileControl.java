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

import com.karuslabs.commons.annotation.Immutable;
import com.karuslabs.commons.locale.YamlResourceBundle;

import java.io.File;
import java.util.*;

import org.bukkit.configuration.file.YamlConfiguration;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;


public class YamlFileControl extends FileControl {
    
    private static final @Immutable List<String> FORMATS = unmodifiableList(asList("yml", "yaml"));
    
    
    public YamlFileControl(File root) {
        super(root);
    }

    @Override
    protected ResourceBundle load(ClassLoader loader, String path) {
        return new YamlResourceBundle(YamlConfiguration.loadConfiguration(new File(root, path)));
    }
    
    @Override
    public @Immutable List<String> getFormats(String bundleName) {
        return FORMATS;
    }
    
}
