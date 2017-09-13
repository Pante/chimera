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
import com.karuslabs.commons.locale.Translations;
import com.karuslabs.commons.util.Get;

import java.util.*;
import javax.annotation.Nullable;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import com.karuslabs.commons.command.completion.Completion;


public class CommandElement extends Element<Command> {
    
    private Plugin plugin;
    private Element<Completion> completion;
    private Element<Translations> translation;
    
    
    public CommandElement(Plugin plugin, Element<Completion> completion, Element<Translations> translation) {
        this(plugin, new HashMap<>(), completion, translation);
    }
    
    public CommandElement(Plugin plugin, Map<String, Command> commands, Element<Completion> completion, Element<Translations> translation) {
        super(commands);
        this.plugin = plugin;
        this.completion = completion;
        this.translation = translation;
    }

    
    @Override
    protected @Nullable Command parse(ConfigurationSection config) {
        Command command = new Command(
            config.getName(),
            Get.orDefault(config.getString("description"), ""),
            Get.orDefault(config.getString("usage"), ""),
            config.getStringList("aliases"),
            plugin,
            CommandExecutor.NONE,
            Get.orDefault(translation.parse(config.getConfigurationSection("translation")), Translations.NONE),
            parseCommands(config.getConfigurationSection("subcommands")),
            parseCompletions(config.getConfigurationSection("completions"))
        );
        
        command.setPermission(Get.orDefault("permission", ""));
        command.setPermissionMessage(Get.orDefault("permission-message", ""));
        
        return command;
    }
    
    protected Map<String, Command> parseCommands(ConfigurationSection config) {
        if (config != null) {
            return config.getKeys(false).stream().collect(toMap(identity(), key -> parse(config.get(key))));
            
        } else {
            return new HashMap<>();
        }
    }
    
    protected Map<Integer, Completion> parseCompletions(ConfigurationSection config) {
        if (config != null) {
            return config.getKeys(false).stream().collect(toMap(Integer::parseInt, key -> completion.parse(config.get(key))));
            
        } else {
           return new HashMap<>(); 
        }
    }
    
}
