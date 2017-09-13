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
package com.karuslabs.commons.command.parser;

import com.karuslabs.commons.locale.*;
import com.karuslabs.commons.locale.resources.*;

import java.io.File;
import java.util.*;
import javax.annotation.Nullable;

import org.bukkit.configuration.ConfigurationSection;

import static java.util.stream.Collectors.toList;


public class TranslationsElement extends Element<Translations> {
    
    private File folder;
    
    
    public TranslationsElement(File folder) {
        this(folder, new HashMap<>());
    }
    
    public TranslationsElement(File folder, Map<String, Translations> translations) {
        super(translations);
        this.folder = folder;
    }

    
    @Override
    protected @Nullable Translations parse(ConfigurationSection config) {
        String bundle = config.getString("bundle");
        if (bundle == null) {
            return null;
        }
        
        List<Resource> resources = config.getStringList("folder").stream().map(path -> new FileResource(new File(folder, path))).collect(toList());
        List<Resource> embedded = config.getStringList("embedded").stream().map(EmbeddedResource::new).collect(toList());
        
        resources.addAll(embedded);
        return new Translations(bundle, new Control(resources.toArray(new Resource[] {})));
    }
    
}
