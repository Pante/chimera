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
package com.karuslabs.commons.command;

import com.karuslabs.commons.command.parser.*;
import com.karuslabs.commons.locale.providers.Provider;

import javax.annotation.Nullable;

import org.bukkit.plugin.Plugin;

import static com.karuslabs.commons.configuration.Configurations.from;


/**
 * Represent the {@code Command}s for a {@code Plugin} and provides facilities for loading and manipulating
 * the {@code Command}s.
 */
public class Commands {
    
    Plugin plugin;
    ProxiedCommandMap map;
    Provider provider;
    
    
    /**
     * Constructs a {@code Commands} for the specified {@code Plugin} with the specified locale {@code Provider}.
     * 
     * @param plugin the Plugin
     * @param provider the locale Provider
     */
    public Commands(Plugin plugin, Provider provider) {
        this(plugin, new ProxiedCommandMap(plugin.getServer()), provider);
    }
    
    /**
     * Constructs a {@code Commands} for the specified {@code Plugin} with 
     * the specified {@code ProxiedCommandMap} and locale {@code Provider}.
     * 
     * @param plugin the Plugin
     * @param map the ProxiedCommandMap
     * @param provider the locale Provider
     */
    public Commands(Plugin plugin, ProxiedCommandMap map, Provider provider) {
        this.plugin = plugin;
        this.map = map;
        this.provider = provider;
    }
    
    
    /**
     * Loads the {@code Command}s for this {@code Plugin} from an embedded YAML file at the specified path,
     * using a {@code Parser} with the default configuration.
     * 
     * @param path the path to the embedded YAML file
     */
    public void load(String path) {
        CompletionElement completion = new CompletionElement();
        CompletionsElement completions = new CompletionsElement(completion);
        TranslationElement translation = new TranslationElement(plugin.getDataFolder(), provider);
        CommandsElement commands = new CommandsElement(null);
        
        CommandElement command = new CommandElement(plugin, commands, translation, completions);
        commands.setCommandElement(command);
        
        load(new Parser(command, translation, completion), path);
    }
    
    /**
     * Loads the {@code Command}s for this {@code Plugin} from an embedded file at the specified path,
     * using the specified {@code Parser}.
     * 
     * @param parser the Parser
     * @param path the path to the embedded file
     */
    public void load(Parser parser, String path) {
        map.registerAll(plugin.getName(), parser.parse(from(getClass().getClassLoader().getResourceAsStream(path))));
    }
    
    
    /**
     * Returns a {@code Command} with the specified name, or {@code null} if the {@code Command} is not present.
     * 
     * @param name the name of the Command
     * @return the Command if present; else null
     */
    public @Nullable Command getCommand(String name) {
        return map.getCommand(name);
    }
    
    /**
     * Returns the {@code ProxiedCommandMap}.
     * 
     * @return the ProxiedCommandMap
     */
    public ProxiedCommandMap getProxiedCommandMap() {
        return map;
    }
    
}
