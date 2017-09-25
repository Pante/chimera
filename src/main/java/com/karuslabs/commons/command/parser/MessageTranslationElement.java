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


public class MessageTranslationElement extends Element<MessageTranslation> {
    
    private File folder;
    
    
    public MessageTranslationElement(File folder) {
        this(folder, new HashMap<>());
    }
    
    public MessageTranslationElement(File folder, Map<String, MessageTranslation> declarations) {
        super(declarations);
        this.folder = folder;
    }

    
    @Override
    protected @Nullable MessageTranslation parse(Object value) {
        MessageTranslation translation = null;
        ConfigurationSection config = null;
        
        if (value instanceof ConfigurationSection) {
            validate(config = (ConfigurationSection) value);
            
            List<Resource> embedded = config.getStringList("embedded").stream().map(EmbeddedResource::new).collect(toList());
            List<Resource> resources = config.getStringList("folder").stream().map(path -> new FileResource(new File(folder, path))).collect(toList());
            resources.addAll(embedded);
            
            translation = new MessageTranslation(config.getString("bundle"), new ExternalControl(resources.toArray(new Resource[] {})));
        }
        
        return translation;
    }
    
    protected void validate(ConfigurationSection config) {
        if (!config.isString("bundle")) {
            throw new ParserException(config.getCurrentPath() + " key: \"bundle\" is missing or contains an invalid value");
            
        } else if (!config.isList("embedded") && !config.isList("folder")) {
            throw new ParserException(config.getCurrentPath() + "must contain either or both keys: \"embedded\" and \"folder\", with lists as values");
        }
    }
    
}
