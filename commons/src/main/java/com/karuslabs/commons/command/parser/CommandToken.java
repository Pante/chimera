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

import com.karuslabs.commons.annotation.JDK9;
import com.karuslabs.commons.command.*;
import com.karuslabs.commons.command.completion.Completion;
import com.karuslabs.commons.locale.MessageTranslation;

import java.util.*;
import javax.annotation.Nullable;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import static com.karuslabs.commons.command.CommandExecutor.NONE;
import static java.util.Arrays.asList;


public class CommandToken extends ReferableToken<Command> {
    
    @JDK9("Set.of(...)")
    private static final Set<String> KEYS = new HashSet<>(asList("aliases", "description", "permission", "permission-message", "usage", "completions", "subcommands", "translation"));
            
    private Plugin plugin;
    private @Nullable Token<Map<String, Command>> subcommands;
    private Token<MessageTranslation> translation;
    private Token<Map<Integer, Completion>> completions;
    
    
    public CommandToken(References references, NullHandle handle, Plugin plugin, @Nullable Token<Map<String, Command>> subcommands, Token<MessageTranslation> translation, Token<Map<Integer, Completion>> completions) {
        super(references, handle);
        this.plugin = plugin;
        this.subcommands = subcommands;
        this.translation = translation;
        this.completions = completions;
    }

    
    @Override
    protected Command getReference(String key) {
        return references.getCommand(key);
    }
    
    @Override
    protected Command register(String key, Command reference) {
        references.command(key, reference);
        return reference;
    }

    @Override
    public boolean isAssignable(ConfigurationSection config, String key) {
        return config.isConfigurationSection(key) && KEYS.containsAll(config.getConfigurationSection(key).getKeys(false));
    }

    @Override
    protected Command get(ConfigurationSection config, String key) {
        config = config.getConfigurationSection(key);
        Command command = new Command(
            config.getName(), plugin, translation.from(config, "translation"),
            config.getString("description", ""),
            config.getString("usage", ""),
            config.getStringList("aliases"),
            NONE,
            subcommands.from(config, "subcommands"),
            completions.from(config, "completions")
        );
        command.setPermission(config.getString("permission", ""));
        command.setPermissionMessage(config.getString("permission-message", ""));
        
        return command;
    }
    
    
    public @Nullable Token<Map<String, Command>> getCommandsToken() {
        return subcommands;
    }
    
    public void setCommandsToken(Token<Map<String, Command>> subcommands) {
        this.subcommands = subcommands;
    }

    @Override
    protected Command getDefaultReference() {
        return Command.NONE;
    }
    
}
