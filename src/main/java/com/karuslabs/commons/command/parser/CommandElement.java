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
import com.karuslabs.commons.locale.MessageTranslation;

import java.util.*;
import javax.annotation.Nullable;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import static java.util.stream.Collectors.toMap;


public class CommandElement extends Element<Command> {

    private Plugin plugin;
    private Element<Map<String, Command>> commands;
    private Element<Completion> completion;
    private Element<MessageTranslation> translation;
    
    
    public CommandElement(Plugin plugin, Element<Map<String, Command>> commands, Element<Completion> completion, Element<MessageTranslation> translation, Map<String, Command> declarations) {
        super(declarations);
        this.plugin = plugin;
        this.commands = commands;
        this.completion = completion;
        this.translation = translation;
    }

    
    @Override
    protected @Nullable Command parse(Object value) {
        Command command = null;
        if (value instanceof ConfigurationSection) {
            ConfigurationSection config = (ConfigurationSection) value;
            command = new Command(
                config.getName(),
                config.getString("description", ""),
                config.getString("usage", ""),
                config.getStringList("aliases"),
                plugin,
                parseTranslation(config.get("translation")),
                CommandExecutor.NONE,
                parseCommands(config.getConfigurationSection("subcommands")),
                parseCompletions(config.getConfigurationSection("completions"))
            );
            command.setPermission(config.getString("permission", ""));
            command.setPermissionMessage(config.getString("permission-message", ""));
        }
        
        return command;
    }
        
    protected MessageTranslation parseTranslation(@Nullable Object value) {
        if (value != null) {
            return translation.parse("translation", value);
            
        } else {
            return MessageTranslation.NONE;
        }
    }
    
    protected Map<String, Command> parseCommands(@Nullable ConfigurationSection config) {
        if (config != null) {
            return commands.parse("subcommands", config);
            
        } else {
            return new HashMap<>();
        }
    }
    
    protected Map<Integer, Completion> parseCompletions(@Nullable ConfigurationSection config) {
        if (config != null) {
            return config.getKeys(false).stream().collect(toMap(Integer::parseInt, key -> completion.parse(key, config.get(key))));
            
        } else {
           return new HashMap<>(); 
        }
    }

    
    public Element<Map<String, Command>> getCommands() {
        return commands;
    }

    public void setCommands(Element<Map<String, Command>> commands) {
        this.commands = commands;
    }

    public Element<Completion> getCompletion() {
        return completion;
    }

    public Element<MessageTranslation> getTranslation() {
        return translation;
    }
    
}
