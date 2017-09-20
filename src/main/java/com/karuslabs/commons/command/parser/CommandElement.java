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

import com.karuslabs.commons.command.*;
import com.karuslabs.commons.command.completion.Completion;
import com.karuslabs.commons.locale.Translation;

import java.util.*;
import javax.annotation.Nullable;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import static java.util.stream.Collectors.toMap;


public class CommandElement extends Element<Command> {
    
    private Plugin plugin;
    private Element<Completion> completion;
    private Element<Translation> translation;
    
    
    public CommandElement(Plugin plugin, Element<Completion> completion, Element<Translation> translation) {
        this(plugin, new HashMap<>(), completion, translation);
    }
    
    public CommandElement(Plugin plugin, Map<String, Command> commands, Element<Completion> completion, Element<Translation> translation) {
        super(commands);
        this.plugin = plugin;
        this.completion = completion;
        this.translation = translation;
    }

    
    @Override
    protected Command parseConfigurationSection(ConfigurationSection config) {        
        Command command = new Command(
            config.getName(),
            config.getString("description", ""),
            config.getString("usage", ""),
            config.getStringList("aliases"),
            plugin,
            CommandExecutor.NONE,
            parseTranslation(config.get("translation")),
            parseCommands(config.getConfigurationSection("subcommands")),
            parseCompletions(config.getConfigurationSection("completions"))
        );
        
        command.setPermission(config.getString("permission", ""));
        command.setPermissionMessage(config.getString("permission-message", ""));
        
        return command;
    }
    
    protected Map<String, Command> parseCommands(@Nullable ConfigurationSection config) {
        Map<String, Command> commands = new HashMap<>();
        if (config != null) {
            config.getKeys(false).forEach(key -> {
                Object value = config.get(key);
                if (value instanceof String && !key.equals(value)) {
                    throw new IllegalArgumentException("non-matching command names: " + key + " and " + value);
                }
                
                Command command = parse(value);
                commands.put(key, command);
                command.getAliases().forEach(alias -> commands.put(alias, command));
            });
        }
        
        return commands;
    }
    
    protected Map<Integer, Completion> parseCompletions(@Nullable ConfigurationSection config) {
        if (config != null) {
            return config.getKeys(false).stream().collect(toMap(Integer::parseInt, key -> completion.parse(config.get(key))));
            
        } else {
           return new HashMap<>(); 
        }
    }
    
    protected Translation parseTranslation(@Nullable Object value) {
        if (value != null) {
            return translation.parse(value);
            
        } else {
            return Translation.NONE;
        }
    }
    
}
