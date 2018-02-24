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


public class Commands {
    
    Plugin plugin;
    ProxiedCommandMap map;
    Provider provider;
    References references;
    CommandProcessor processor;
    
    
    public Commands(Plugin plugin, Provider provider, References references, CommandProcessor processor) {
        this(plugin, new ProxiedCommandMap(plugin.getServer()), provider, references, processor);
    }
    
    public Commands(Plugin plugin, ProxiedCommandMap map, Provider provider, References references, CommandProcessor processor) {
        this.plugin = plugin;
        this.map = map;
        this.provider = provider;
        this.references = references;
        this.processor = processor;
    }
    
    
    public void load(String path) {
        map.registerAll(plugin.getName(), loadParser().parse(from(getClass().getClassLoader().getResourceAsStream(path))));
    }
    
    protected Parser loadParser() {
        return Parsers.newParser(plugin, plugin.getDataFolder(), references, NullHandle.NONE, provider);
    }
    
    
    public void register(CommandExecutor executor) {
        processor.process(map, executor);
    }
    
    
    public @Nullable Command getCommand(String name) {
        return map.getCommand(name);
    }
    
    public ProxiedCommandMap getProxiedCommandMap() {
        return map;
    }
    
    public References getReferences() {
        return references;
    }
    
    public CommandProcessor getProcessor() {
        return processor;
    }
    
    
    public static Commands simple(Plugin plugin, Provider provider) {
        References references = new References();
        CommandProcessor processor = new CommandProcessor(
            new HashSet<>(asList(new InformationProcessor(), new LiteralProcessor(), new RegisteredProcessor(references))), new NamespaceResolver()
        );
        
        return new Commands(plugin, provider, references, processor);
    }
    
}
