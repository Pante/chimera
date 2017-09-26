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
import javax.annotation.Nonnull;

import org.bukkit.configuration.ConfigurationSection;

import static java.util.Arrays.copyOf;
import static java.lang.System.arraycopy;


public class TranslationElement extends Element<MessageTranslation> {
    
    private File folder;
    
    
    public TranslationElement(File folder) {
        this(folder, new HashMap<>());
    }
    
    public TranslationElement(File folder, Map<String, MessageTranslation> declarations) {
        super(declarations);
        this.folder = folder;
    }

    
    @Override
    protected MessageTranslation handleNull(ConfigurationSection config, String key) {
        return MessageTranslation.NONE;
    }
    
    @Override
    protected boolean check(ConfigurationSection config, String key) {
        config = config.getConfigurationSection(key);
        if (!config.isString("bundle")) {
            throw new ParserException("Missing or invalid value for: " + config.getCurrentPath() + "." + key + ".bundle");
            
        } else if (!config.isList("embedded") && !config.isList("folder")) {
            String path = config.getCurrentPath() + "." + key;
            throw new ParserException("Missing keys or invalid values for : " + path + ".embedded and/or " + path + ".folder");
            
        } else {
            return true;
        }
    }

    @Override
    protected @Nonnull MessageTranslation handle(ConfigurationSection config, String key) {
        ConfigurationSection translation = config.getConfigurationSection(key);
        List<String> embeddedResources = translation.getStringList("embedded");
        List<String> fileResources = translation.getStringList("folder");
        
        Resource[] embedded = embeddedResources.stream().map(EmbeddedResource::new).toArray(Resource[]::new);
        Resource[] files = fileResources.stream().map(path -> new FileResource(new File(folder, path))).toArray(Resource[]::new);
        
        Resource[] resources = copyOf(embedded, embedded.length + files.length);
        arraycopy(files, 0, resources, embedded.length, files.length);
        
        return new MessageTranslation(translation.getString("bundle"), new ExternalControl(resources));
    }
    
}
