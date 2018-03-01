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

import com.karuslabs.commons.command.annotation.processors.*;
import com.karuslabs.commons.command.parser.*;
import com.karuslabs.commons.locale.providers.Provider;

import java.util.HashSet;
import javax.annotation.Nullable;

import org.bukkit.plugin.Plugin;

import static com.karuslabs.commons.configuration.Configurations.from;
import static java.util.Arrays.asList;


/**
 * Represent the {@code Command}s for a {@code Plugin} and provides facilities for loading and manipulating
 * the {@code Command}s.
 */
public class Commands {
    
    Plugin plugin;
    ProxiedCommandMap map;
    Provider provider;
    References references;
    CommandProcessor processor;
    
    
    /**
     * Constructs a {@code Commands} for the specified {@code Plugin} with the specified locale {@code Provider}.
     * 
     * @param plugin the Plugin
     * @param provider the locale Provider
     * @param references the references
     * @param processor the processor for processing the annotations
     */
    public Commands(Plugin plugin, Provider provider, References references, CommandProcessor processor) {
        this(plugin, new ProxiedCommandMap(plugin.getServer()), provider, references, processor);
    }
    
    /**
     * Constructs a {@code Commands} for the specified {@code Plugin} with 
     * the specified {@code ProxiedCommandMap} and locale {@code Provider}.
     * 
     * @param plugin the Plugin
     * @param map the ProxiedCommandMap
     * @param provider the locale Provider
     * @param references the references
     * @param processor the processor for processing the annotations
     */
    public Commands(Plugin plugin, ProxiedCommandMap map, Provider provider, References references, CommandProcessor processor) {
        this.plugin = plugin;
        this.map = map;
        this.provider = provider;
        this.references = references;
        this.processor = processor;
    }
    
    
    /**
     * Loads the {@code Command}s for this {@code Plugin} from an embedded YAML file at the specified path,
     * using the {@code Parser} specified by {@link #loadParser()}.
     * 
     * @param path the path to the embedded YAML file
     */
    public void load(String path) {
        map.registerAll(plugin.getName(), loadParser().parse(from(getClass().getClassLoader().getResourceAsStream(path))));
    }
    
    /**
     * Creates a {@code Parser}.
     * 
     * The default implementation returns the {@code Parser} specifeid by {@link Parsers#newParser(Plugin, File, References, NullHandle, Provider)} .
     * 
     * Subclasses may override this method to customise the loading of the specified {@code Parser}.
     * 
     * @return the Parser
     */
    protected Parser loadParser() {
        return Parsers.newParser(plugin, plugin.getDataFolder(), references, NullHandle.NONE, provider);
    }
    
    
    /**
     * Registers the specified {@code CommandExecutor} to the {@code Command}s using the specified {@code Namespace} and other related annotations.
     *
     * @param executor the annotated executor
     * @throws IllegalArgumentException if the specified CommandExecutor has no
     */
    public void register(CommandExecutor executor) {
        processor.process(map, executor);
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
    
    /**
     * Returns the {@code References}.
     * 
     * @return the references
     */
    public References getReferences() {
        return references;
    }
    
    /**
     * Returns the {@code CommandProcessor}.
     * 
     * @return the CommandProcessor
     */
    public CommandProcessor getProcessor() {
        return processor;
    }
    
    
    /**
     * Creates a {@code Commands} with the specified {@code Plugin} and {@code Provider} with the default settings.
     * 
     * @param plugin the plugin
     * @param provider the provider
     * @return the Commands
     */
    public static Commands simple(Plugin plugin, Provider provider) {
        References references = new References();
        CommandProcessor processor = new CommandProcessor(
            new HashSet<>(asList(new InformationProcessor(), new LiteralProcessor(), new RegisteredProcessor(references))), new NamespaceResolver()
        );
        
        return new Commands(plugin, provider, references, processor);
    }
    
}
