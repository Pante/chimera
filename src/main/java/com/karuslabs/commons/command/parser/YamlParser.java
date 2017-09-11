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
import com.karuslabs.commons.command.completers.*;
import com.karuslabs.commons.locale.Translations;

import java.io.InputStream;
import java.util.*;

import org.bukkit.configuration.ConfigurationSection;

import static com.karuslabs.commons.configuration.Configurations.from;


public class YamlParser implements Parser {
    
    private Map<String, Command> commands;
    private Map<String, Translations> translations;
    private Map<String, Completer> completers;
    
    
    public YamlParser() {
        commands = new HashMap<>();
        translations = new HashMap<>();
        completers = new HashMap<>();
        completers.put("PLAYER_NAMES", Completer.PLAYER_NAMES);
        completers.put("WORLD_NAMES", Completer.WORLD_NAMES);
    }
    
    
    @Override
    public List<Command> parse(InputStream stream) {
        ConfigurationSection config = from(stream);
        return null;
    }
    
    protected void parseDefinitions(ConfigurationSection config) {
        
    }
    
    protected List<Command> parseCommands(ConfigurationSection config) {
        return null;
    }
    
    protected Command parseCommand(ConfigurationSection config) {
        return null;
    }
    
    protected Translations parseTranslations(ConfigurationSection config) {
        return null;
    }
    
    protected Completer parseCompleter(ConfigurationSection config, String path) {
        if (config.getString(path) != null && completers.containsKey(path)) {
            return completers.get(path);
            
        } else {
            return new CachedCompleter(config.getStringList(path));
        }
    }
    
}
