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

import com.karuslabs.commons.command.Command;
import com.karuslabs.commons.command.completion.Completion;
import com.karuslabs.commons.locale.MessageTranslation;

import java.util.List;
import javax.annotation.Nullable;

import org.bukkit.configuration.ConfigurationSection;

import static java.util.Collections.EMPTY_LIST;
import static java.util.stream.Collectors.toList;


public class Parser {
    
    private Element<Command> command;
    private Element<MessageTranslation> translation;
    private Element<Completion> completion;
    
    
    public Parser(Element<Command> command, Element<MessageTranslation> translation, Element<Completion> completion) {
        this.command = command;
        this.translation = translation;
        this.completion = completion;
    }
    
    
    public List<Command> parse(ConfigurationSection config) {
        parseDeclarations(config.getConfigurationSection("declare"));
        return parseCommands(config.getConfigurationSection("commands"));
    }
    
    protected void parseDeclarations(@Nullable ConfigurationSection config) {
        if (config != null) {
            parseDeclaration(completion, config.getConfigurationSection("completions"));
            parseDeclaration(translation, config.getConfigurationSection("translations"));
            parseDeclaration(command, config.getConfigurationSection("commands"));
        }
    }
    
    protected void parseDeclaration(Element<?> element, ConfigurationSection config) {
        if (config != null) {
            config.getKeys(false).forEach(key -> element.declare(config, key));
        }
    }
    
    protected List<Command> parseCommands(@Nullable ConfigurationSection config) {
        if (config != null) {
            return config.getKeys(false).stream().map(key -> command.parse(config, key)).collect(toList());
            
        } else {
            return EMPTY_LIST;
        }
    }  
    
}
