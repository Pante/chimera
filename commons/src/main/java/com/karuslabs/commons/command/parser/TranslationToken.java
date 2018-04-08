/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
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

import com.karuslabs.commons.command.References;
import com.karuslabs.commons.locale.*;
import com.karuslabs.commons.locale.providers.Provider;
import com.karuslabs.commons.locale.resources.*;

import java.io.File;

import org.bukkit.configuration.ConfigurationSection;

import static com.karuslabs.commons.locale.MessageTranslation.NONE;
import static java.lang.System.arraycopy;
import static java.util.Arrays.copyOf;


public class TranslationToken extends ReferableToken<MessageTranslation> {
    
    private File folder;
    private Provider provider;
    
    
    public TranslationToken(References register, NullHandle handle, File folder, Provider provider) {
        super(register, handle);
        this.folder = folder;
        this.provider = provider;
    }
    
    
    @Override
    protected MessageTranslation getNull(ConfigurationSection config, String key) {
        return NONE;
    }
    
    @Override
    protected MessageTranslation getReference(String key) {
        return references.getTranslation(key);
    }

    @Override
    protected MessageTranslation register(String key, MessageTranslation reference) {
        references.translation(key, reference);
        return reference;
    }

    @Override
    public boolean isAssignable(ConfigurationSection config, String key) {
        config = config.getConfigurationSection(key);
        return config != null && config.isString("bundle") && ((config.get("embedded") != null && config.isList("embedded")) || (config.get("folder") != null && config.isList("folder")));
    }

    @Override
    protected MessageTranslation get(ConfigurationSection config, String key) {
        ConfigurationSection translation = config.getConfigurationSection(key);
        
        Resource[] embedded = translation.getStringList("embedded").stream().map(EmbeddedResource::new).toArray(Resource[]::new);
        Resource[] folders = translation.getStringList("folder").stream().map(path -> new ExternalResource(new File(folder, path))).toArray(Resource[]::new);
        
        Resource[] resources = copyOf(embedded, embedded.length + folders.length);
        arraycopy(folders, 0, resources, embedded.length, folders.length);
        
        return new MessageTranslation(translation.getString("bundle"), new ExternalControl(resources), provider);
    }

    @Override
    protected MessageTranslation getDefaultReference() {
        return MessageTranslation.NONE;
    }
    
}
