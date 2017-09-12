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
import com.karuslabs.commons.locale.Translations;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import static java.util.stream.Collectors.toList;


public class Parser {
    
    private Element<Command> commands;
    private Element<Completion> completions;
    private Element<Translations> translations;
    
    
    public Parser(Element<Command> commands, Element<Completion> completions, Element<Translations> translations) {
        this.commands = commands;
        this.completions = completions;
        this.translations = translations;
    }
    
    
    public List<Command> parse(ConfigurationSection config) {
        parseDefinitions(config.getConfigurationSection("define"));
        return config.getKeys(false).stream().filter(key -> !key.equals("define")).map(key -> commands.parse(config.get(key))).collect(toList());
    }
    
    protected void parseDefinitions(ConfigurationSection config) {
        if (config != null) {
            parseDefinitions(config.getConfigurationSection("completions"), completions);
            parseDefinitions(config.getConfigurationSection("translations"), translations);
            parseDefinitions(config.getConfigurationSection("commands"), commands);
        }
    }
    
    protected void parseDefinitions(ConfigurationSection config, Element<?> element) {
        if (config != null) {
            config.getKeys(false).forEach(key -> element.define(key, config.get(key)));
        }
    }

    
    public Element<Command> getCommands() {
        return commands;
    }

    public Element<Completion> getCompletions() {
        return completions;
    }

    public Element<Translations> getTranslations() {
        return translations;
    }
    
}
